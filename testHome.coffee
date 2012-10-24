#!/usr/bin/env coffee

Browser = require 'zombie'
expect = require 'expect.js'

port = process.argv[2]

should = (name, url, callback) ->
  browser = new Browser( { "maxWait": 10000, "waitFor": 10000 } )
  browser.visit "http://localhost:#{port}#{url}", -> callback(browser)

should 'Should show title', '/', (browser) ->
  expect(browser.text 'title').to.be 'CodeStory - Devoxx Fight'

should 'Should show teaser', '/', (browser) ->
  expect(browser.text '#vote').to.contain 'Create your personal schedule for Devoxx.'

should 'Should show sessions', '/planning.html', (browser) ->
  expect(browser.text '#session-759 .title').to.be 'Android Development Code Lab (Hands-on Labs)'
  expect(browser.text '#session-759 .speaker').to.be 'Nick Butcher, Richard Hyndman @BOF 1 from 09:30 to 12:30'
  expect(browser.text '#session-759 .description').to.contain ''

  expect(browser.text '#session-761 .title').to.be 'Modular Architecture Today (University)'
  expect(browser.text '#session-761 .speaker').to.be 'Kirk Knoernschild @Room 8 from 09:30 to 12:30'
  expect(browser.text '#session-761 .description').to.contain ''

should 'Should register while logged in', '/planning.html', (browser) ->
  expect(browser.text '#auth a').to.be 'Log In'
  expect(browser.cookies().get 'userId').to.be undefined
  expect(browser.text '#session-759 a.register').to.be 'Register'
  expect(browser.text '#session-760 a.register').to.be 'Register'
  expect(browser.text '#session-761 a.register').to.be 'Register'

  browser.clickLink '#login', ->
    expect(browser.text '#auth a').to.be 'Log Out'
    expect(browser.cookies().get 'userId').to.be '42'
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

        browser.clickLink '#logout', ->
          expect(browser.cookies().get 'userId').to.be undefined
          expect(browser.text '#auth a').to.be 'Log In'
