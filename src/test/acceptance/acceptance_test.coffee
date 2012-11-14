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
			browser.text('title').should.equal "Code-Story Fight for Devoxx"
			done()

	it 'should display scores', (done) ->
		browser = new Browser
		browser.visit home, ->
			browser.text('#leftKeyword').should.contain "AngularJS"
			browser.text('#leftScore').should.contain "37"
			browser.text('#rightKeyword').should.contain "JavaFX"
			browser.text('#rightScore').should.contain "51"
			done()

	it 'should display scores for java and scala', (done) ->
		browser = new Browser
		browser.visit "#{home}/fight/java/scala", ->
			browser.text('#leftKeyword').should.contain "java"
			browser.text('#rightKeyword').should.contain "scala"
			done()

#			browser.clickLink '#leftKeyword a', ->
#				browser.location.href.should.contain '/planning.html?q=AngularJS'
#				done()
