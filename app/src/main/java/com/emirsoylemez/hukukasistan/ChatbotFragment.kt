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
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.adapter = chatAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observeViewModel() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.setMessages(messages)
            if (messages.isNotEmpty()) {
                binding.chatRecyclerView.scrollToPosition(messages.size - 1)
            }
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString()
            if (messageText.isNotBlank()) {
                val userMessage = ChatMessage.User(messageText)
                viewModel.addMessage(userMessage)
                binding.messageEditText.text.clear()

                // Yeni backend bağlantısı çağrılıyor
                getBotResponseFromFastApi(messageText)
            }
        }
    }

    private fun getBotResponseFromFastApi(messageText: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // SoruRequest oluşturuluyor
                val request = SoruRequest(soru_metni = messageText)

                // Retrofit ile FastAPI'ye istek atılıyor
                val response = RetrofitClient.instance.soruSor(request)

                // Cevap alınıp UI'a ekleniyor
                val botReply = response.cevap
                viewModel.addMessage(ChatMessage.Bot(botReply))

                // Eğer kaynaklar da gösterilmek istenirse response.kaynaklar kullanılabilir

            } catch (e: Exception) {
                Log.e("ChatbotFragment", "FastAPI Hatası: ", e)
                viewModel.addMessage(ChatMessage.Bot("Üzgünüm, şu an bağlantı kurulamadı."))
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
