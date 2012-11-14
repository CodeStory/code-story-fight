Browser = require('zombie')
should = require('chai').should()

home = 'http://127.0.0.1:8080'

# Uncheck to debug
#Browser.debug = true

describe 'Server', ->
	it 'should be live', (done) ->
		browser = new Browser()
		browser.visit home, ->
			browser.success.should.equal true
			done()

	it 'should show hello world in the homepage', (done) ->
		browser = new Browser
		browser.visit home, ->
			browser.text('h1').should.equal "Hello World"
			done()

	it 'should display scores', (done) ->
		browser = new Browser
		browser.visit home, ->
			browser.text('#leftKeyword').should.contain "AngularJS"
			browser.text('#leftScore').should.contain "37"
			browser.text('#rightKeyword').should.contain "JavaFX"
			browser.text('#rightScore').should.contain "42"
			done()
