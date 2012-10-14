Browser = require 'zombie'
expect = require 'expect.js'

port = process.argv[2]

should = (name, url, callback) ->
  browser = new Browser()
  browser.visit "http://localhost:#{port}" + url, -> callback(browser)

should 'Should show title', '/', (browser) ->
  expect(browser.text 'title').to.be 'CodeStory - Devoxx Fight'

should 'Should show teaser', '/', (browser) ->
  expect(browser.text '#vote').to.contain 'Create your personal schedule for Devoxx.'

should 'Should got to planning to vite', '/', (browser) ->
  browser.clickLink '#vote a', ->
    expect(browser.text '.day').to.contain 'Monday'

should 'Should show sessions', '/planning.html', (browser) ->
  expect(browser.text '#session01 .title').to.be 'Scala m\'a tuer !'
  expect(browser.text '#session01 .speaker').to.be 'John Doe'
  expect(browser.text '#session01 .description').to.contain 'Lorem ipsum'
  expect(browser.text '#session01 .tags .tag').to.contain 'Scala'
  expect(browser.text '#session01 .tags .tag').to.contain 'Troll'

  expect(browser.text '#session03 .title').to.be 'Java m\'a tuer !'
  expect(browser.text '#session03 .speaker').to.be 'Jane Doe'
  expect(browser.text '#session03 .description').to.contain 'Lorem ipsum'
  expect(browser.text '#session03 .tags .tag').to.contain 'Java'

should 'Should register', '/planning.html', (browser) ->
  expect(browser.text '#session01 a.register').to.be 'Register'
  expect(browser.text '#session02 a.register').to.be 'Register'
  expect(browser.text '#session03 a.register').to.be 'Register'

  browser.clickLink '#session01 a.register', ->
    expect(browser.text '#session01 a.register').to.be 'Unregister'
    expect(browser.text '#session02 a.register').to.be 'Register'
    expect(browser.text '#session03 a.register').to.be 'Register'

should 'Should unregister', '/planning.html', (browser) ->
  browser.clickLink '#session01 a.register', ->
    browser.clickLink '#session01 a.register', ->
      expect(browser.text '#session01 a.register').to.be 'Register'
      expect(browser.text '#session02 a.register').to.be 'Register'
      expect(browser.text '#session03 a.register').to.be 'Register'
