def call(String imageName, String gcpRegion, String gcpProject, String artifactRepo) {
    def lastSuccessful = sh(
        script: "gcloud artifacts docker images list ${gcpRegion}-docker.pkg.dev/${gcpProject}/${artifactRepo}/${imageName} --format='get(tags)' | grep build- | sort -r | sed -n '2p'",
        returnStdout: true
    ).trim()

    if (lastSuccessful) {
        echo "Rolling back to ${lastSuccessful}"
        sh """
            docker-compose pull frontend
            docker-compose up -d --no-deps --build frontend
        """
    } else {
        echo "No previous successful image available for rollback!"
    }
}
