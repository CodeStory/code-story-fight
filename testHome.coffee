Browser = require 'zombie'
expect = require 'expect.js'

port = process.argv[2]

should = (name, callback) ->
  browser = new Browser()
  browser.visit "http://localhost:#{port}/", -> callback(browser)

should 'Should show title', (browser) ->
  expect(browser.text 'title').to.be 'CodeStory - Fight for Devoxx'

should 'Should show sessions', (browser) ->
  expect(browser.text '#session01 .title').to.be 'Scala m\'a tuer !'
  expect(browser.text '#session01 .speaker').to.be 'John Doe'
  expect(browser.text '#session01 .description').to.contain 'Lorem ipsum'
  expect(browser.text '#session01 .tags .tag').to.contain 'Scala'
  expect(browser.text '#session01 .tags .tag').to.contain 'Troll'

  expect(browser.text '#session03 .title').to.be 'Java m\'a tuer !'
  expect(browser.text '#session03 .speaker').to.be 'Jane Doe'
  expect(browser.text '#session03 .description').to.contain 'Lorem ipsum'
  expect(browser.text '#session03 .tags .tag').to.contain 'Java'

should 'Should register', (browser) ->
  expect(browser.text '#session01 a.register').to.be 'Register'
  expect(browser.text '#session02 a.register').to.be 'Register'
  expect(browser.text '#session03 a.register').to.be 'Register'

  browser.clickLink '#session01 a.register', ->
    expect(browser.text '#session01 a.register').to.be 'Unregister'
    expect(browser.text '#session02 a.register').to.be 'Register'
    expect(browser.text '#session03 a.register').to.be 'Register'

should 'Should unregister', (browser) ->
  browser.clickLink '#session01 a.register', ->
    browser.clickLink '#session01 a.register', ->
      expect(browser.text '#session01 a.register').to.be 'Register'
      expect(browser.text '#session02 a.register').to.be 'Register'
      expect(browser.text '#session03 a.register').to.be 'Register'
