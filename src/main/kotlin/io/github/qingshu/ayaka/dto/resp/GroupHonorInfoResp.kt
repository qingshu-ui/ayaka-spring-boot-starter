package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class GroupHonorInfoResp(
    @JsonProperty("group_id") val groupId: Long = 0,
    @JsonProperty("current_talkative") val currentTalkative: CurrentTalkative = CurrentTalkative(),
    @JsonProperty("talkative_list") val talkativeList: List<OtherHonor> = emptyList(),
    @JsonProperty("performer_list") val performerList: List<OtherHonor> = emptyList(),
    @JsonProperty("legend_list") val legendList: List<OtherHonor> = emptyList(),
    @JsonProperty("strong_newbie_list") val strongNewbieList: List<OtherHonor> = emptyList(),
    @JsonProperty("emotion_list") val emotionList: List<OtherHonor> = emptyList(),
) {
    data class CurrentTalkative(
        @JsonProperty("user_id") val userId: Long = 0,
        @JsonProperty("nickname") val nickname: String = EMPTY_STRING,
        @JsonProperty("avatar") val avatar: String = EMPTY_STRING,
        @JsonProperty("day_count") val dayCount: Int = 0,
    )

    data class OtherHonor(
        @JsonProperty("user_id") val userId: Long = 0,
        @JsonProperty("nickname") val nickname: String = EMPTY_STRING,
        @JsonProperty("avatar") val avatar: String = EMPTY_STRING,
        @JsonProperty("description") val description: String = EMPTY_STRING,
    )
}
