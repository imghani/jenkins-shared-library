def call(String serviceName, String deployLogDir) {

    sh 'echo "LIBRARY VERSION CHECK: deployContainer.groovy LOADED"'

    // Force bash explicitly (because Jenkins 'sh' uses /bin/sh by default)
    sh(
        label: "Deploy ${serviceName}",
        script: """
            bash -lc '
                set -euo pipefail
                set -x

                echo "===== DEBUG START ====="
                pwd
                whoami
                ls -la /home/imran || true
                ls -la /home/imran/docker-compose.yml || true
                docker-compose version || true
                echo "===== DEBUG END ====="

                cd /home/imran

                docker-compose -f docker-compose.yml pull ${serviceName}
                docker-compose -f docker-compose.yml up -d --no-deps ${serviceName}
            '
        """
    )
}

