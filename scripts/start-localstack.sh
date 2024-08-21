#!/usr/bin/env bash

set -euo pipefail

echo "Creating topics"
awslocal --endpoint-url=http://localhost:4566 sns create-topic --name=transaction-done --region=us-east-1
awslocal --endpoint-url=http://localhost:4566 sqs create-queue --queue-name=queue-test --region=us-east-1
awslocal sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:transaction-done \
 --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:queue-test
echo "Created topics"
