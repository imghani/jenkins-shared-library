def call(String serviceName, String deployLogDir) {

    sh(
        label: "Deploy ${serviceName}",
        script: """
            bash -lc '
                set -e

                cd /home/imran

                docker-compose \
                  --env-file workspace/image-tags.env \
                  pull ${serviceName}

                docker-compose \
                  --env-file workspace/image-tags.env \
                  up -d --no-deps ${serviceName}
            '
        """
    )
}

