#!/bin/bash

TIME="10"
URL="https://gitty.shop/gitty/gitlab/pipeline"

curl -s --max-time $TIME --location --request POST $URL -H 'Content-Type: application/json' --data-raw '{ "status": "'"$1"'", "url": "'"$CI_PROJECT_URL/pipelines/$CI_PIPELINE_ID"'", "mr_id": "'"$CI_MERGE_REQUEST_ID"'", "project_id": "'"$CI_MERGE_REQUEST_PROJECT_ID"'"}' > /dev/null


### EXAMPLES
#TIME="10"
#URL="https://gitty.shop:8443/gitty/gitlab/pipeline"
#CI_PROJECT_URL="google.com"
#CI_PIPELINE_ID="123"
#CI_MERGE_REQUEST_ID="565"
#CI_MERGE_REQUEST_PROJECT_ID="4"