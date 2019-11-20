img="islamahmad/eaproj-content-filter-ms:1.0.10"

build:
	docker build -t $(img) .

push:
	docker push $(img)

config:
	kubectl apply -f k8s-config.yaml

deploy:
	kubectl apply -f k8s-deploy.yaml

#all: build push config deploy


# ===== Maven =====
maven-rebuild:
	mvn clean && mvn install

# ===== Docker =====
docker-build: maven-rebuild
	docker build -t ${img} .

docker-run:
	docker run -p 8080:8092 ${img}

docker-login:
	docker login

docker-push: docker-login docker-build
	docker push ${img}

k8-install:
	kubectl apply -f k8s-deploy.yaml

k8-delete:
	kubectl delete -f k8s-deploy.yaml

k8-repush-restart: k8-delete docker-push k8-install
