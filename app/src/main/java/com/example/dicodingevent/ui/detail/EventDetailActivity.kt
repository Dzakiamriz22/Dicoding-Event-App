package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.ActivityEventDetailBinding

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding
    private val args: EventDetailActivityArgs by navArgs()
    private val eventDetailViewModel: EventDetailViewModel by viewModels<EventDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Event Detail"

        eventDetailViewModel.fetchEventDetail(args.id)

        eventDetailViewModel.eventDetail.observe(this) { eventDetail ->
            if (eventDetail != null) {
                Glide.with(binding.root.context).load(eventDetail.imageLogo).into(binding.imgEventPhoto)
                binding.tvEventName.text = eventDetail.name
                binding.tvEventOwner.text = eventDetail.ownerName
                binding.tvEventDate.text = eventDetail.beginTime
                binding.tvEventRemainingQuota.text = (eventDetail.quota ?: 0).toString() // Jika quota null, tampilkan 0

                // Pastikan quota dan registrants tidak null sebelum melakukan perhitungan
                val quota = eventDetail.quota ?: 0
                val registrants = eventDetail.registrants ?: 0

                // Kalkulasi sisa kuota
                val remainingQuota = quota - registrants
                binding.tvEventRemainingQuota.text = remainingQuota.toString()

                binding.tvDesc.text = HtmlCompat.fromHtml(eventDetail.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
                setRegisterButton(eventDetail.link.toString())
            }
        }

        eventDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setRegisterButton(url: String) {
        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}
