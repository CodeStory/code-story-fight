#!/usr/bin/env coffee

Browser = require 'zombie'
expect = require 'expect.js'

port = process.argv[2]

should = (name, url, callback) ->
  browser = new Browser()
  browser.visit "http://localhost:#{port}#{url}", -> callback(browser)

should 'Should show title', '/', (browser) ->
  expect(browser.text 'title').to.be 'CodeStory - Devoxx Fight'

should 'Should show teaser', '/', (browser) ->
  expect(browser.text '#vote').to.contain 'Create your personal schedule for Devoxx.'

should 'Should show sessions', '/planning.html', (browser) ->
  expect(browser.text '#session-759 .title').to.be 'Android Development Code Lab (Hands-on Labs)'
  expect(browser.text '#session-759 .speaker').to.be 'Nick Butcher -'
  expect(browser.text '#session-759 .description').to.contain ''

  expect(browser.text '#session-761 .title').to.be 'Modular Architecture Today (University)'
  expect(browser.text '#session-761 .speaker').to.be 'Kirk Knoernschild -'
  expect(browser.text '#session-761 .description').to.contain ''

should 'Should register', '/planning.html', (browser) ->
  expect(browser.text '#session-759 a.register').to.be 'Register'
  expect(browser.text '#session-760 a.register').to.be 'Register'
  expect(browser.text '#session-761 a.register').to.be 'Register'

  browser.clickLink '#session-759 a.register', ->
    expect(browser.text '#session-759 a.register').to.be 'Unregister'
    expect(browser.text '#session-760 a.register').to.be 'Register'
    expect(browser.text '#session-761 a.register').to.be 'Register'

    browser.clickLink '#session-759 a.register', ->
      expect(browser.text '#session-759 a.register').to.be 'Register'
      expect(browser.text '#session-760 a.register').to.be 'Register'
      expect(browser.text '#session-761 a.register').to.be 'Register'
