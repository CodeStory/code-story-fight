$.getJSON('planning.json', function (json) {
  $("body").html(Hogan.compile($('#talks-template').html()).render(json));
});