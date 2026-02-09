def call(String serviceName, String deployLogDir) {

    sh "echo 'LIBRARY VERSION CHECK: deployContainer.groovy UPDATED AT $(date)'"

    sh """
        set -euxo pipefail

        echo "===== DEBUG START ====="
        pwd
        whoami
        ls -la /home/imran
        ls -la /home/imran/docker-compose.yml
        docker-compose version
        echo "===== DEBUG END ====="

        cd /home/imran

        docker-compose -f docker-compose.yml pull ${serviceName}
        docker-compose -f docker-compose.yml up -d --no-deps ${serviceName}
    """
}

