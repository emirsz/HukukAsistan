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
import com.emirsoylemez.hukukasistan.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class ChatbotFragment : Fragment() {

    // 1. ViewBinding değişkenleri sınıf içine taşındı.
    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!

    private lateinit var generativeModel: GenerativeModel


    // Adapter için bir değişken
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)

        // EKLENDİ: Modeli API anahtarınız ile başlatıyoruz.
        // BuildConfig.GEMINI_API_KEY, Gradle'da tanımladığımız anahtardır.
        generativeModel = GenerativeModel(
            modelName = "gemini-2.5-flash", // Hızlı ve genel kullanım için iyi bir model
            apiKey = BuildConfig.GEMINI_API_KEY
        )


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
                getBotResponse(messageText)
            }
        }
    }



    private fun getBotResponse(messageText: String) {

        // 1. Prompt'u oluştur
        val prompt = buildPrompt(messageText)

        // 2. Coroutine başlat
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = generativeModel.generateContent(prompt)

                response.text?.let { botReply ->
                    val botMessage = ChatMessage.Bot(botReply)
                    chatAdapter.addMessage(botMessage)
                    binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
                }
            } catch (e: Exception) {
                Log.e("ChatbotFragment", "API Hatası: ", e)
                val errorMessage =
                    ChatMessage.Bot("Üzgünüm, bir hata oluştu. Lütfen tekrar deneyin.")
                chatAdapter.addMessage(errorMessage)
                binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }
    }


    private fun buildPrompt(userMessage: String): String {
        return """
Sen bir Türk Hukuku yapay zeka asistanısın.

KURALLAR:
1. YALNIZCA Türk hukuku ile ilgili sorulara cevap ver.
2. Türk hukuku ile ilgisi olmayan sorulara SADECE şu cümleyle cevap ver:
   "Bu soru Türk hukuku kapsamında değildir."
3. Hukuki sorulara verdiğin cevaplar:
   - Orta uzunlukta olsun (3–6 cümle).
   - Gereksiz ayrıntıya girme.
   - Dilekçe örneği istenirse ver.
   - Akademik dil kullanma.
   - Mümkünse ilgili kanun veya maddeyi belirt.
   - "Bu metin hukuki tavsiye niteliği taşımaz" gibi bir uyarı ekle.
4. Hukuk dışı sorularda ASLA ek açıklama yapma.

SORU:
$userMessage
""".trimIndent()
    }


    // 6. Bellek sızıntılarını önlemek için onDestroyView eklendi.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
