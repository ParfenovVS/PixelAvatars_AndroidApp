package com.github.parfenovvs.pixelavatars

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.github.parfenovvs.pixelavatars.model.SpriteType
import com.github.parfenovvs.pixelavatars.model.SpriteType.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_avatar_generate.*

class AvatarGenerateFragment : BottomSheetDialogFragment() {

    var onGenerateClickListener: (SpriteType, String) -> Unit = { _, _ -> }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_avatar_generate, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateButton.setOnClickListener {
            val seed = seedEditText.text.toString().trim()
            if (seed.isEmpty()) {
                Toast.makeText(requireContext(), R.string.type_seed, LENGTH_SHORT).show()
            } else {
                onGenerateClickListener.invoke(getCheckedSpiritType(), seed)
            }
        }
    }

    private fun getCheckedSpiritType(): SpriteType = when (spriteTypeGroup.checkedRadioButtonId) {
        R.id.maleRadioButton -> MALE
        R.id.femaleRadioButton -> FEMALE
        R.id.randomRadioButton -> RANDOM
        else -> throw IllegalStateException("Not supported sprite type")
    }
}