#!/usr/bin/env coffee

Browser = require 'zombie'
expect = require 'expect.js'

port = 8080
port = process.argv[2] if process.argv[2]?

should = (name, url, callback) ->
  browser = new Browser( { "maxWait": 10000, "waitFor": 10000 } )
  browser.visit "http://localhost:#{port}#{url}", -> callback(browser)

should 'Should show title', '/', (browser) ->
  expect(browser.text 'title').to.be 'CodeStory - Devoxx Fight'

should 'Should show teaser', '/', (browser) ->
  expect(browser.text '#vote').to.contain 'Create your personal schedule for Devoxx.'

should 'Should show toc','/planning.html', (browser) ->
  expect(browser.text '.toc a').to.contain '9:30'

should 'Should show sessions', '/planning.html', (browser) ->
  expect(browser.text '#talk-759 h1').to.be 'Android Development Code Lab (Hands-on Labs)'
  expect(browser.text '#talk-759 .speaker').to.be 'Nick Butcher, Richard Hyndman @BOF 1 from 09:30 to 12:30'
  expect(browser.text '#talk-759 p').to.contain 'Dive into some of the latest'

  expect(browser.text '#talk-761 h1').to.be 'Modular Architecture Today (University)'
  expect(browser.text '#talk-761 .speaker').to.be 'Kirk Knoernschild @Room 8 from 09:30 to 12:30'
  expect(browser.text '#talk-761 p').to.contain 'Modularity is coming to the Java platform!'

should 'Should redirect to authentication when user star', '/planning.html', (browser) ->
  browser.clickLink '#talk-759 .star', ->
    expect(browser.location.href).to.contain '/planning.html'
    expect(browser.text '#auth a').to.be 'Log Out'

should 'Should star while logged in', '/planning.html', (browser) ->
  expect(browser.text '#auth a').to.be 'Log In'
  expect(browser.cookies().get 'userId').to.be undefined
  expect(browser.query '#talk-759 .star').to.be.ok()
  expect(browser.query '#talk-760 .star').to.be.ok()
  expect(browser.query '#talk-761 .star').to.be.ok()
  expect(browser.query '#talk-759 .starred').to.not.be.ok()
  expect(browser.query '#talk-760 .starred').to.not.be.ok()
  expect(browser.query '#talk-761 .starred').to.not.be.ok()

  browser.clickLink '#login', ->
    expect(browser.text '#auth a').to.be 'Log Out'
    expect(browser.text '#screenName').to.be '@arnold'
    expect(browser.cookies().get 'userId').to.be '42'
    expect(browser.cookies().get 'screenName').to.be 'arnold'
    expect(browser.query '#talk-759 .star').to.be.ok()
    expect(browser.query '#talk-759 .starred').to.not.be.ok()

    browser.clickLink '#talk-759 .star', ->
      expect(browser.query '#talk-759 .starred').to.be.ok()

      browser.clickLink '#talk-759 .star', ->
        expect(browser.query '#talk-759 .star').to.be.ok()
        expect(browser.query '#talk-759 .starred').to.not.be.ok()

        browser.clickLink '#logout', ->
          expect(browser.cookies().get 'userId').to.be undefined
          expect(browser.text '#auth a').to.be 'Log In'
