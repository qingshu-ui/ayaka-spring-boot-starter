package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GroupHonorInfoResp(
    @JSONField(name = "group_id") private val groupId: Long?,
    @JSONField(name = "current_talkative") private val currentTalkative: CurrentTalkative?,
    @JSONField(name = "talkative_list") private val talkativeList: List<OtherHonor>?,
    @JSONField(name = "performer_list") private val performerList: List<OtherHonor>?,
    @JSONField(name = "legend_list") private val legendList: List<OtherHonor>?,
    @JSONField(name = "strong_newbie_list") private val strongNewbieList: List<OtherHonor>?,
    @JSONField(name = "emotion_list") private val emotionList: List<OtherHonor>?,
) {
    data class CurrentTalkative(
        @JSONField(name = "user_id") private val userId: Long?,
        @JSONField(name = "nickname") private val nickname: String?,
        @JSONField(name = "avatar") private val avatar: String?,
        @JSONField(name = "day_count") private val dayCount: Int?,
    )

    data class OtherHonor(
        @JSONField(name = "user_id") private val userId: Long?,
        @JSONField(name = "nickname") private val nickname: String?,
        @JSONField(name = "avatar") private val avatar: String?,
        @JSONField(name = "description") private val description: String?,
    )
}
