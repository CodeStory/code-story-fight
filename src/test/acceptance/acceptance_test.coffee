Browser = require('zombie')
should = require('chai').should()

port = process.env.PORT || 8080
home = "http://127.0.0.1:#{port}/"

describe 'Server', ->
	it 'should be live', (done) ->
		browser = new Browser()
		browser.visit home, ->
			browser.success.should.equal true
			done()

	it 'should show hello world in the homepage', (done) ->
		browser = new Browser
		browser.visit home, ->
			browser.text('title').should.equal "Code-Story Fight for Devoxx"
			done()

	it 'should display scores', (done) ->
		browser = new Browser
		browser.visit home, ->
			browser.field('input[id="leftKeyword"]').value.should.equal 'AngularJS'
			browser.text('#leftScore').should.contain "37"
			browser.field('input[id="rightKeyword"]').value.should.equal 'JavaFX'
			browser.text('#rightScore').should.contain "51"
			done()

	it 'should display scores for java and scala', (done) ->
		browser = new Browser
		browser.visit "#{home}fight/java/scala", ->
			browser.field('input[id="leftKeyword"]').value.should.equal "java"
			browser.field('input[id="rightKeyword"]').value.should.equal "scala"
			done()

#			browser.clickLink '#leftKeyword a', ->
#				browser.location.href.should.contain '/planning.html?q=AngularJS'
#				done()