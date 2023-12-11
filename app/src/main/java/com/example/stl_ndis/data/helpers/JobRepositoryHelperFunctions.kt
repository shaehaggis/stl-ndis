package com.example.stl_ndis.data.helpers

import com.example.stl_ndis.data.models.JobAssignmentDTO
import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.data.models.NDISJobDTO
import com.example.stl_ndis.data.models.SupportWorker

fun createHashMap(jobs: List<NDISJob>): HashMap<String, Int> {

    val hashMap = HashMap<String, Int>()

    jobs.forEachIndexed { index, ndisJob ->
        ndisJob.jobId.let { jobId ->
            hashMap[jobId] = index
        }
    }

    return hashMap
}

fun addAssignmentsToJobs(
    assignments: List<JobAssignmentDTO>,
    hashMap: HashMap<String, Int>,
    jobsList: List<NDISJob>
): MutableList<NDISJob> {

    val jobs = jobsList.toMutableList()

    assignments.forEach { assignment ->
        if (assignment.supportWorkerID != null && assignment.firstName != null && assignment.surname != null){

            // create a new support worker object
            val supportWorker = SupportWorker(
                supportWorkerID = assignment.supportWorkerID,
                firstName = assignment.firstName,
                surname = assignment.surname
            )

            // find index of job to add assignment to
            val jobIndex = hashMap[assignment.jobID]

            // add the support worker to the list of assigned support workers for that job
            jobIndex?.let { index ->
                val job = jobs.getOrNull(index)
                job?.let {
                    it.assignedSupportWorkers = it.assignedSupportWorkers.orEmpty() + supportWorker
                    jobs[index] = it
                }
            }
        }
    }

    return jobs
}

fun convertDomainToDto(job: NDISJob, userID: String?): NDISJobDTO {
    return NDISJobDTO(
        userId = userID,
        startDate = job.startDate,
        startTime = job.startTime,
        hours = job.hours,
        status = job.status,
        serviceCategory = job.serviceCategory,
        description = job.description,
        pickupLocation = job.pickupLocation
    )
}