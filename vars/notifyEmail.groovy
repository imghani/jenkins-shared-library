def call(String status, String recipient, Map details = [:]) {

    // Default safe values
    def job = details.get('job', env.JOB_NAME)
    def build = details.get('build', env.BUILD_NUMBER)
    def commit = details.get('commit', env.GIT_COMMIT ?: "N/A")

    def subject = ""
    def body = ""

    if (status == "success") {
        subject = "✅ SUCCESS: ${job} #${build}"
        body = """
Deployment Succeeded!

Job: ${job}
Build Number: ${build}
Commit: ${commit}

Everything is running correctly.
"""
    } else if (status == "failure") {
        subject = "❌ FAILURE: ${job} #${build}"
        body = """
Deployment Failed!

Job: ${job}
Build Number: ${build}
Commit: ${commit}

Rollback attempted automatically.
"""
    } else {
        error "notifyEmail: Unknown status '${status}'"
    }

    mail(
        to: recipient,
        subject: subject,
        body: body
    )
}
