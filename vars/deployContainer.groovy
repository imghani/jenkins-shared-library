def call(String serviceName, String deployLogDir) {

    sh 'echo "LIBRARY VERSION CHECK: deployContainer.groovy LOADED"'

    sh(
        label: "Deploy ${serviceName}",
        script: """
            bash -lc '
                set -euo pipefail
                set -x

                export BACKEND_IMAGE=us-central1-docker.pkg.dev/paklawassistapp/backend/workspace-backend:latest
                export FRONTEND_IMAGE=us-central1-docker.pkg.dev/paklawassistapp/frontend/workspace-frontend:latest

                cd /home/imran

                docker-compose pull ${serviceName}
                docker-compose up -d --no-deps ${serviceName}
            '
        """
    )
}
