Activate the xquartz:
open -a XQuartz

Allow connections:
xhost +localhost

Set DISPLAY:
export DISPLAY=host.docker.internal:0

Make sure Minikube uses Docker

1. minikube start --driver=docker

Use Minikube’s Docker daemon

2. eval $(minikube -p minikube docker-env)

Build your image

3.  docker build -t amirdirin/shoppingcart-app:latest .

Deploy to Kubernetes
4. kubectl apply -f shoppingcart_deployment.yaml

check the pods status become Running

READY   STATUS    RESTARTS
1/1     Running   0


5. to delete the minikube
   kubectl delete pod <pod-name>
   kubectl delete pod avgspeed-app-5984c69657-958w6