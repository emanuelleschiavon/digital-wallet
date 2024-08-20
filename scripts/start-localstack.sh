#!/usr/bin/env bash

set -euo pipefail

echo "Creating topics"
awslocal --endpoint-url=http://localhost:4566 sns create-topic --name=transaction-done --region=us-east-1
echo "Created topics"
