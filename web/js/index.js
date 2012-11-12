$(document).ready(function () {
  $('#front a').click(function () {
    $('#front').animate({
      top:'-10000px'
    }, 3000, function () {
      $('#front').hide();
    });
    $('#back').toggle();
  });

  $('#counter').countdown({
    timestamp:(new Date(2012, 10, 12, 8, 0, 0, 0)).getTime()
  });
})
