#!/bin/bash

NOTIFY_TIME="10"
NOTIFY_TIME="https://gitty.shop/gitty/gitlab/pipeline"
STATUS="success"

curl -v --max-time $NOTIFY_TIME --location --request POST $NOTIFY_TIME -H 'Content-Type: application/json' --data-raw '{ "'"$STATUS"'": "success", "url": "'"$CI_PROJECT_URL/pipelines/$CI_PIPELINE_ID"'", "pipeline_id": "'"$CI_PIPELINE_ID"'", "project_id": "'"$CI_PROJECT_ID"'"}'

### EXAMPLES
#TIME="10"
#URL="https://gitty.shop:8443/gitty/gitlab/pipeline"
#CI_PROJECT_URL="google.com"
#CI_PIPELINE_ID="123"
#CI_MERGE_REQUEST_ID="565"
#CI_MERGE_REQUEST_PROJECT_ID="4"