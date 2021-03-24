jQuery(document).ready(function ($) {
  let ws;
  let connected = false;
  let token;

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

  function render(_json) {
    let orders = _json;
    if (orders.orderType === "CLOSE") {
      deal(orders);
      return true;
    }

    let table = orders.isBuy ? "buy" : "sell";
    let bgColor = orders.isBuy ? "tomato" : "dodgerblue";
    $(`#table-${table}`).append(`<tr style="background-color: ${bgColor}" class="price-${orders.price} 
        idx-${orders.userIdx}"><td>${orders.price}</td><td>${orders.amount}</td>
        <td><button type="button" class="deal" onclick="deal(${orders})">deal</button></td></tr>`);
  }

  deal = function (_orders) {
    $.ajax({
      url: "/deal",
      type: "POST",
      data: JSON.stringify(_orders),
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
      url: "http://localhost:8081/api/v1/orders/post",
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
    ws = new WebSocket('ws://localhost:8080/bin');

    ws.onopen = function () {
      send();
      connected = true;
      setConnected();
      console.log("connected...");
    };

    ws.onmessage = function (data) {
      let fr = new FileReader();

      fr.onload = function () {
        let parse = JSON.parse(this.result);
        render(parse);
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