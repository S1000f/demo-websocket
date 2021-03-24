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

  function render(_json) {
    let orders = _json;
    if (orders.orderType === "CLOSE") {
      deal(orders);
      return true;
    }

    let table = orders.isBuy ? "buy" : "sell";
    let bgColor = orders.isBuy ? "tomato" : "dodgerblue";
    $(`#table-${table}`).append(`<tr style="background-color: ${bgColor}" class="price-${orders.price} idx-${orders.userIdx}">
            <td>${orders.price}</td><td>${orders.amount}</td>
            <td><button type="button" class="deal" onclick="deal(${orders})">
            deal</button></td></tr>`);
  }

  deal = function(_orders) {
    $.ajax({
      url: "/deal",
      type: "POST",
      data: JSON.stringify(_orders),
      contentType: "application/json",
      success : function (result) {

      }
    });
  }

  function order() {
    let priceInput = $("#input-price").val();
    let amountInput = $("#input-amount").val();
    let typeInput = $("#input-type-buy").prop("checked");

    let request = {
      marketPair : "BTC-KRW",
      isBuy : typeInput,
      price : priceInput,
      amount : amountInput,
      orderType : "SPOT",
      userIdx: token
    }

    $.ajax({
      url : "http://localhost:8081/api/v1/orders/post",
      type : "POST",
      data : JSON.stringify(request),
      contentType : "application/json",
      success : function(result){
      }
    });
  }

  function closeOrder(_this) {

    console.log(_this);
  }

  function clear() {
    $("#table-buy").children().remove();
    $("#table-sell").children().remove();
    $.get("/clear", function (response) {
    });
  }

  function connect() {
    ws = new WebSocket('ws://localhost:8080/bin');

    ws.onopen = function () {
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
    token = $("#token").val();
    let uint8array = new TextEncoder("utf-8").encode(prefix + token);

    ws.send(uint8array);
  }

  function disconnect() {
    if (ws != null) {
      ws.close();
    }
    connected = false;
    setConnected();
    clear();
    console.log("disconnected...");
  }

  $("#connect").click(function() {
    connect();
  });

  $("#send").click(function() {
    send();
  });

  $("#disconnect").click(function() {
    disconnect();
  });

  $("#clear").click(function () {
    clear();
  });

  $("#order").click(function () {
    order();
  });

});