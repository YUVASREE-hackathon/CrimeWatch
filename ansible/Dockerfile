FROM python:3.13-alpine

RUN pip install --no-cache-dir ansible-core==2.20.1

WORKDIR /workspace
ENTRYPOINT ["ansible-playbook"]
