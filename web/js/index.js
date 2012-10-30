!function (d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (!d.getElementById(id)) {
    js = d.createElement(s);
    js.id = id;
    js.src = "//platform.twitter.com/widgets.js";
    fjs.parentNode.insertBefore(js, fjs);
  }
}(document, "script", "twitter-wjs");

$(document).ready(function () {
  $('#toggle').click(function () {
    $('#front').slideToggle('slow');
    $('#back').toggle();
  });

  $('#counter').countdown({
    timestamp:(new Date(2012, 10, 12, 8, 0, 0, 0)).getTime()
  });

  $('#score_left').text(Math.floor((Math.random() * 99) + 1));
  $('#score_right').text(Math.floor((Math.random() * 99) + 1));
});


