$.getJSON('planning.json', function (json) {
  $("body").html(Hogan.compile($('#talks-template').html()).render(json));
});

$('a.register').live('click', function() {
  if ($(this).html() == 'Register') {
    $(this).html('Unregister');
  } else {
    $(this).html('Register');
  }
});