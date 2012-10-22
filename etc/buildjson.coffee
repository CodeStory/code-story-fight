#!/usr/bin/env coffee 

_ = require('underscore')
http = require('http')

transformPresentation = (data) ->
  summary: data.summary

transformTalks = (data) ->
  _.chain(data)
  .filter((talk) -> talk.speaker?)
  .sortBy((talk) -> talk.fromTime)
  .map((talk) ->
    id: talk.id,
    title: talk.title,
    room: talk.room
    speaker: talk.speaker,
    day: talk.fromTime[0..9],
    from: talk.fromTime[11..15],
    to: talk.toTime[11..15],
    type: talk.type,
    speakers: _.pluck(talk.speakers, 'speaker').join ',',
  )
  .groupBy((talk) -> talk.day)
  .map((talks, day) ->
    day: day,
    slots: _.chain(talks).groupBy((talk) -> talk.from).map((talks, slot) ->
      slot: slot,
      talks: talks
    ).value()
  ).value()

retrieve = (url, action) ->
  http.get url, (response) ->
    scheduleData = ""
    response.on 'data', (data) -> scheduleData += data
    response.on 'end', -> action(scheduleData)

retrieve 'http://cfp.devoxx.com/rest/v1/events/7/schedule', (body) ->
  schedule = {days: transformTalks(JSON.parse body)}
  console.log JSON.stringify(schedule, null, " ")


