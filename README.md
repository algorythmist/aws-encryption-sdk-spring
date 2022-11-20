# AWS Security Demo

These instructions demonstrate how to create AWS users and secret keys: https://www.codeproject.com/Articles/5129195/AWS-Key-Management-System-KMS-to-Encrypt-and-Decry

KMS Concepts: https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html

AWS Encryption SDK: https://docs.aws.amazon.com/encryption-sdk/latest/developer-guide/introduction.html

KMS localstack: https://docs.localstack.cloud/aws/kms/

---
# Start docker

- install docker
- from the project directory
```text
docker compose up
```

# Setup aws local

- Install python >= 3.9
- pip install awscli
- pip install  awscli-local
- pip install localstack

To create a secret key:

```text
awslocal kms create-key
```

To find keys

```text
awslocal kms list-keys
```
