#!/usr/bin/env bash
set -e

./mvnw clean install && docker-compose up -d