package se.ohou.commerce.domain.salesapplication

// NOTE: 특정 entity 에 포함되면 data class 를 기본으로?
data class SalesContent(
    val goods: Goods,
    val option: List<Option>,
) {
    data class Goods(
        val name: String,
        val imageUrl: String,
        val subImageUrls: List<String>,
        val memoOption: MemoOption?,
    ) {
        data class MemoOption(
            val title: String,
            val isEssential: Boolean,
        )
    }

    data class Option(
        val optionValues: List<String>,
        val imageUrls: List<String>,
    )
}