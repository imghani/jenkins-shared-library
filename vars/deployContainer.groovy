echo "===== DEBUG START ====="
pwd
whoami
ls -la
ls -la /home/imran
ls -la /home/imran/docker-compose.yml
docker-compose version
echo "===== DEBUG END ====="

cd /home/imran

docker-compose -f /home/imran/docker-compose.yml pull ${serviceName}
docker-compose -f /home/imran/docker-compose.yml up -d --no-deps ${serviceName}
