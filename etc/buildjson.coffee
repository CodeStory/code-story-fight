#!/usr/bin/env coffee 

_ = require('underscore')
http = require('http')
Promise = require("promised-io/promise").Promise
all = require("promised-io/promise").all

promises = []

retrieve = (url) ->
  promise = new Promise()
  http.get url, (response) ->
    scheduleData = ""
    response.on 'data', (data) -> scheduleData += data
    response.on 'end', () -> promise.resolve JSON.parse(scheduleData)
  promise

transformTalks = (data) ->
  _.chain(data)
  .filter((talk) -> talk.speaker?)
  .sortBy((talk) -> talk.fromTime)
  .map((talk) ->
    id: talk.id
    title: talk.title
    presentationUri: talk.presentationUri
    summary: ''
    room: talk.room
    speaker: talk.speaker
    day: talk.fromTime[0..9]
    from: talk.fromTime[11..15]
    to: talk.toTime[11..15]
    type: talk.type
    speakers: _.pluck(talk.speakers, 'speaker').join ','
  ).map((talk) ->
    promises.push retrieve(talk.presentationUri).then (presentation) ->
      talk.summary = presentation.summary
      talk.tags = _.pluck(presentation.tags, 'name')
    talk
  ).groupBy((talk) -> talk.day)
  .map((talks, day) ->
    day: day,
    slots: _.chain(talks).groupBy((talk) -> talk.from).map((talks, slot) ->
      slot: slot,
      talks: talks
    ).value()
  ).value()

retrieve('http://cfp.devoxx.com/rest/v1/events/7/schedule').then (talks) ->
  schedule = {days: transformTalks(talks)}
  all(promises).then () ->
    console.log JSON.stringify(schedule, null, " ")

