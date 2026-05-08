import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emirsoylemez.hukukasistan.ChatMessage
import com.emirsoylemez.hukukasistan.databinding.ItemChatBotBinding
import com.emirsoylemez.hukukasistan.databinding.ItemChatUserBinding

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messages = mutableListOf<ChatMessage>()

    fun setMessages(newMessages: List<ChatMessage>) {
        messages = newMessages.toMutableList()
        notifyDataSetChanged()
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position]) {
            is ChatMessage.User -> 0
            is ChatMessage.Bot -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = ItemChatUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            UserViewHolder(binding)
        } else {
            val binding = ItemChatBotBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            BotViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val message = messages[position]) {
            is ChatMessage.User -> {
                (holder as UserViewHolder).binding.tvUserMessage.text = message.text
            }
            is ChatMessage.Bot -> {
                (holder as BotViewHolder).binding.tvBotMessage.text = message.text
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    class UserViewHolder(val binding: ItemChatUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    class BotViewHolder(val binding: ItemChatBotBinding) :
        RecyclerView.ViewHolder(binding.root)
}
