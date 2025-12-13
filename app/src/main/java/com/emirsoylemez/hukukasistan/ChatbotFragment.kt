package com.emirsoylemez.hukukasistan

import ChatAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirsoylemez.hukukasistan.databinding.FragmentChatbotBinding


class ChatbotFragment : Fragment() {

    // 1. ViewBinding değişkenleri sınıf içine taşındı.
    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!

    // Adapter için bir değişken
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 2. Tüm arayüz mantığı onViewCreated içine taşındı.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        // 3. Adapter ve LayoutManager ayarlandı.
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.adapter = chatAdapter
        val layoutManager = LinearLayoutManager(context)
        binding.chatRecyclerView.layoutManager = layoutManager
    }

    private fun setupClickListeners() {
        // Geri butonuna tıklama olayı
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // 4. Gönder butonu mantığı
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString()
            if (messageText.isNotBlank()) {
                // Kullanıcı mesajını listeye ekle
                val userMessage = ChatMessage.User(messageText)
                chatAdapter.addMessage(userMessage)

                // Mesajı gönderdikten sonra en alta kaydır
                binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)

                // EditText'i temizle
                binding.messageEditText.text.clear()

                // 5. Bot cevabını simüle et
                simulateBotResponse()
            }
        }
    }

    private fun simulateBotResponse() {
        // Gerçekte burada yapay zeka API'nize istek atacaksınız.
        // Şimdilik 1 saniye sonra otomatik bir cevap verelim.
        binding.root.postDelayed({
            val botMessage = ChatMessage.Bot("Mesajınızı aldım, en kısa sürede inceliyorum.")
            chatAdapter.addMessage(botMessage)
            binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        }, 1000)
    }

    // 6. Bellek sızıntılarını önlemek için onDestroyView eklendi.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
