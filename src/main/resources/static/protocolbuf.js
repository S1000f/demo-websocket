jQuery(document).ready(function ($) {
  let ws;
  let connected = false;
  let token;
  let orderBooks = [];

  function setConnected() {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#connect-message").html(connected ? "state: connected" : "state: disconnected");
  }
  setConnected();

  (function generateToken() {
    let table = ["a", "b", "c", "d", "e", "f"];
    let userToken = "";
    for (let i = 0; i < 10; i++) {
      let num = Math.floor(Math.random() * Math.floor(16));
      if (num > 9) {
        userToken += table[num - 10];
      } else {
        userToken += num;
      }
    }

    token = userToken;
    $("#token").val(token);
    console.log("your token: " + userToken);
  })();

  function render(_jsonString) {
    let orders = JSON.parse(_jsonString);
    if (orders.orderType === "CLOSE") {
      close(orders);
      return true;
    }
    orderBooks[orders.date] = orders;

    let table = orders.isBuy ? "buy" : "sell";
    let bgColor = orders.isBuy ? "tomato" : "dodgerblue";
    $(`#table-${table}`).append(`<tr style="background-color: ${bgColor}" class="price-${orders.price} 
        idx-${orders.userIdx} date-${orders.date}"><td>${orders.price}</td><td>${orders.amount}</td>
        <td><button type="button" class="deal" onclick="deal(${orders.date})">deal</button></td></tr>`);
  }

  function close(_orders) {
    let target = _orders.date;
    orderBooks.splice(orderBooks.indexOf(target), 1);

    $(`.date-${target}`).remove();
  }

  deal = function (_param) {
    $.ajax({
      url: "/deal",
      type: "POST",
      data: JSON.stringify(orderBooks[_param]),
      contentType: "application/json",
      success: function (response) {
        console.log("deal: " + response);
      }
    });
  }

  function order() {
    let priceInput = $("#input-price").val();
    let amountInput = $("#input-amount").val();
    let typeInput = $("#input-type-buy").prop("checked");

    let request = {
      marketPair: "BTC-KRW",
      isBuy: typeInput,
      price: priceInput,
      amount: amountInput,
      orderType: "SPOT",
      userIdx: token
    }

    $.ajax({
      url: "http://192.168.0.36:8081/api/v1/orders/post",
      type: "POST",
      data: JSON.stringify(request),
      contentType: "application/json",
      success: function (response) {
        let comment = response ? "successfully" : "with failure";
        console.log("the order has been placed " + comment);
      }
    });
  }

  function clear(_cache) {
    $("#table-buy").children().remove();
    $("#table-sell").children().remove();

    if (_cache === true) {
      $.get("/clear", function (response) {
        let comment = response ? "success" : "failed";
        console.log("order queue cleared: " + comment);
      });
    }
  }

  function connect() {
    ws = new WebSocket('ws://192.168.0.36:8080/bin');

    ws.onopen = function () {
      send();
      connected = true;
      setConnected();
      console.log("connected...");
    };

    ws.onmessage = function (data) {
      let fr = new FileReader();

      fr.onload = function () {
        render(this.result);
      };

      fr.readAsText(data.data);
    };
  }

  function send() {
    let prefix = "token:";
    let uint8array = new TextEncoder("utf-8").encode(prefix + token);

    ws.send(uint8array);

    $("#send").html("your token delivered");
  }

  function disconnect() {
    if (ws != null) {
      ws.close();
    }
    connected = false;
    setConnected();
    clear(false);
    console.log("disconnected...");
  }

  $("#connect").click(function () {
    connect();
  });

  $("#send").click(function () {
    send();
  });

  $("#disconnect").click(function () {
    disconnect();
  });

  $("#clear").click(function () {
    clear(true);
  });

  $("#order").click(function () {
    order();
  });

});