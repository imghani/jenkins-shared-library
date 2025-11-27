def call(String imageName, String tag, String gcpRegion, String gcpProject, String artifactRepo) {
    withCredentials([file(credentialsId: :contentReference[oaicite:0]{index=0}, variable: 'GCP_KEY')]) {
        def registry = "${gcpRegion}-docker.pkg.dev/${gcpProject}/${artifactRepo}/${imageName}"

        sh """
            set -e
            echo "🔐 Authenticating with GCP..."
            gcloud auth activate-service-account --key-file=\$GCP_KEY
            gcloud auth configure-docker ${gcpRegion}-docker.pkg.dev

            echo "🏷 Tagging & pushing Docker image..."
            docker tag ${imageName}:${tag} ${registry}:${tag}
            docker tag ${imageName}:latest ${registry}:latest

            docker push ${registry}:${tag}
            docker push ${registry}:latest

            echo "🧹 Cleaning old images (keeping latest 4 by date + protecting latest tag)..."

            DIGESTS_TO_DELETE=\$(gcloud artifacts docker images list ${registry} \
                --format="get(tags,digest,createTime)" \
                | sort -rk3 \
                | awk 'NR>4 {print \$2}')

            if [ -z "\$DIGESTS_TO_DELETE" ]; then
                echo "✅ No old images to delete."
            else
                echo "\$DIGESTS_TO_DELETE" | while read d; do
                    # Extra guard: skip malformed lines
                    if ! echo "\$d" | grep -q "sha256:"; then
                        echo "Skipping invalid digest → \$d"
                        continue
                    fi

                    echo "🗑 Deleting → \$d"
                    gcloud artifacts docker images delete -q ${registry}@\$d --delete-tags
                done
            fi

            echo "♻ Local prune..."
            docker image prune -f
            echo "🚀 Done."
        """
    }
}
