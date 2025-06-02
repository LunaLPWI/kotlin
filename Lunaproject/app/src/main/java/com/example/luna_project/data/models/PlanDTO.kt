package com.example.luna_project.data.models

import java.time.LocalDateTime

class PlanDTO (
      id: Long,
      name:String,
      interval: Integer,
      repeat: Integer,
      created_at: LocalDateTime,
      plan_id: String
)
