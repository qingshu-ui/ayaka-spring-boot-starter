package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GroupHonorInfoResp(
    @JSONField(name = "group_id") val groupId: Long?,
    @JSONField(name = "current_talkative") val currentTalkative: CurrentTalkative?,
    @JSONField(name = "talkative_list") val talkativeList: List<OtherHonor>?,
    @JSONField(name = "performer_list") val performerList: List<OtherHonor>?,
    @JSONField(name = "legend_list") val legendList: List<OtherHonor>?,
    @JSONField(name = "strong_newbie_list") val strongNewbieList: List<OtherHonor>?,
    @JSONField(name = "emotion_list") val emotionList: List<OtherHonor>?,
) {
    data class CurrentTalkative(
        @JSONField(name = "user_id") val userId: Long?,
        @JSONField(name = "nickname") val nickname: String?,
        @JSONField(name = "avatar") val avatar: String?,
        @JSONField(name = "day_count") val dayCount: Int?,
    )

    data class OtherHonor(
        @JSONField(name = "user_id") val userId: Long?,
        @JSONField(name = "nickname") val nickname: String?,
        @JSONField(name = "avatar") val avatar: String?,
        @JSONField(name = "description") val description: String?,
    )
}
