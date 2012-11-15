Browser = require('zombie')
should = require('chai').should()

port = process.env.PORT || 8080
home = "http://127.0.0.1:#{port}"

describe 'Server', ->
	it 'should show the homepage', (done) ->
		browser = new Browser
		browser.visit "#{home}/", ->
			browser.text('title').should.equal 'Code-Story Fight for Devoxx'
			done()

	it 'should display scores for AngularJS and JavaFX', (done) ->
		browser = new Browser
		browser.visit "#{home}/fight/AngularJS/JavaFX", ->
			browser.field('#leftKeyword').value.should.equal 'AngularJS'
			browser.field('#rightKeyword').value.should.equal 'JavaFX'
			browser.text('#leftScore').should.contain '37'
			browser.text('#rightScore').should.contain '51'
			done()

	it 'should display scores for java and scala', (done) ->
		browser = new Browser
		browser.visit "#{home}/fight/java/scala", ->
			browser.field('input[id="leftKeyword"]').value.should.equal 'java'
			browser.field('input[id="rightKeyword"]').value.should.equal 'scala'
			browser.text('#leftScore').should.contain '819'
			browser.text('#rightScore').should.contain '207'
			done()
