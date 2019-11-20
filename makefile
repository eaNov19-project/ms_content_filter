img="islamahmad/eaproj-content-filter-ms:1.0.1"

build:
    docker build -t $(img) .

push:
    docker push $(img)

config:
	kubectl apply -f k8s-config.yaml

deploy:
	kubectl apply -f k8s-deploy.yaml

#all: build push config deploy