package se.ohou.commerce.sample.entity

import se.ohou.commerce.sample.Sample

class SampleEntity(
    val id: Int,
) {
    fun toDomain(): Sample = Sample(id = id)

    constructor(sample: Sample) : this(id = sample.id)
}
