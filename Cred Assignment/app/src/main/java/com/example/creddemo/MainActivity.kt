package com.example.creddemo

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.creddemo.DataClass.Data
import com.example.creddemo.databinding.ActivityMainBinding
import me.tankery.lib.circularseekbar.CircularSeekBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.min
import kotlin.math.pow
import kotlin.math.round


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var pageOpen = 1
    private var emiDuration = 0
    private var emiAmount = "0"
    private var r = 0.0104
    private var maxi =0
    private var mini = 0
    private var creditValue = 0.0
    private var selectionText1 = ""
    private var selectionText2 = ""
    private var selectionText3 = ""
    private var selectionText4 = "Go back to home"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonOnClick()
        getData()
        changeEmiOnClick()
        selectionButtonOnClick()

    }


    private fun buttonOnClick() {
        binding.amountPage.seekBar.setOnSeekBarChangeListener(object : CircularSeekBar.OnCircularSeekBarChangeListener{
            override fun onProgressChanged(
                circularSeekBar: CircularSeekBar?,
                progress: Float,
                fromUser: Boolean
            ) {

            }

            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                creditValue = (seekBar!!.progress.toDouble()/100)*maxi
                binding.amountPage.enterAmount.setText(creditValue.toInt().toString())
            }

        })

        binding.closeButton.setOnClickListener {
            binding.selectionButton.selectionText.text = selectionText1
            removePageVisibility()
            pageOpen=1
            binding.amountPage.seekBar.progress = 0.0F
            creditValue = 0.0
            binding.amountPage.enterAmount.setText(creditValue.toInt().toString())
            binding.amountPage.root.visibility = View.VISIBLE
            binding.planLayout.dropdownEmi.visibility=View.VISIBLE
            binding.amountLayout.dropdownAmount.visibility=View.VISIBLE
            binding.bankLayout.dropdownBank.visibility=View.VISIBLE
        }
        binding.emiPage.goButton.setOnClickListener {
            binding.emiPage.customEmi.visibility = View.VISIBLE
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.emiPage.root.windowToken, 0)
            val creditValue1 = binding.amountPage.enterAmount.text.toString().toInt()
            val mo = binding.emiPage.customMonth.text.toString().toInt()
            val emin =round((creditValue1 * r* (1 + r).pow(mo))/((1 + r).pow(mo) -1))
            emiDuration = mo
            emiAmount = emin.toString()
            binding.emiPage.customEmi.text = emiAmount
        }

        binding.emiPage.emiCard1.setOnClickListener{
            emi()
            customEmi()
            binding.emiPage.card1Tick.visibility =View.VISIBLE
            emiDuration = 12
            emiAmount = binding.emiPage.card1Text.text.toString()

        }
        binding.emiPage.emiCard2.setOnClickListener{
            emi()
            customEmi()
            binding.emiPage.card2Tick.visibility =View.VISIBLE
            emiDuration = 9
            emiAmount = binding.emiPage.card2Text.text.toString()
        }
        binding.emiPage.emiCard3.setOnClickListener{
            emi()
            customEmi()
            binding.emiPage.card3Tick.visibility =View.VISIBLE
            emiDuration = 6
            emiAmount = binding.emiPage.card3Text.text.toString()
        }
        binding.emiPage.emiCard4.setOnClickListener{
            emi()
            customEmi()
            binding.emiPage.card4Tick.visibility =View.VISIBLE
            emiDuration = 3
            emiAmount = binding.emiPage.card4Text.text.toString()
        }
        binding.amountLayout.dropdownAmount.setOnClickListener{
            removePageVisibility()
            binding.amountPage.root.visibility = View.VISIBLE
            val translate = AnimationUtils.loadAnimation(this, R.anim.translation)
            binding.amountPage.root.startAnimation(translate)
            pageOpen = 1
            binding.selectionButton.selectionText.text = selectionText1
        }
        binding.planLayout.dropdownEmi.setOnClickListener {
            removePageVisibility()
            binding.amountLayout.root.visibility = View.VISIBLE
            binding.emiPage.root.visibility = View.VISIBLE
            val translate = AnimationUtils.loadAnimation(this, R.anim.translation)
            binding.emiPage.root.startAnimation(translate)
            pageOpen = 2
            binding.selectionButton.selectionText.text = selectionText2
        }
    }
    private fun changeEmiOnClick() {
        binding.emiPage.changePlan.setOnClickListener {
            binding.emiPage.customMonthText.visibility = View.VISIBLE
            binding.emiPage.customEmiText.visibility = View.VISIBLE
            binding.emiPage.customMonth.visibility = View.VISIBLE
            binding.emiPage.customMonthText2.visibility = View.VISIBLE
            binding.emiPage.goButton.visibility =View.VISIBLE
            emi()
        }

    }
    private fun selectionButtonOnClick() {
        binding.selectionButton.root.setOnClickListener {
            if(binding.amountPage.enterAmount.text.isEmpty()){

                binding.amountPage.seekBar.progress = 0.0F
                creditValue = 0.0
                binding.amountPage.enterAmount.setText(creditValue.toInt().toString())
            }
            val creditValue1 = binding.amountPage.enterAmount.text.toString().toInt()
            if(pageOpen == 1){
                if(creditValue1 in mini..maxi) {
                    removePageVisibility()
                    binding.emiPage.root.visibility = View.VISIBLE
                    binding.selectionButton.selectionText.text = selectionText2
                    val translate = AnimationUtils.loadAnimation(this, R.anim.translation)
                    binding.emiPage.root.startAnimation(translate)
                    pageOpen = 2
                    binding.amountLayout.root.visibility = View.VISIBLE
                    binding.amountLayout.amount.text = "₹" + binding.amountPage.enterAmount.text
                    setEMI(creditValue1)
                }else{
                    Toast.makeText(this, "Enter a value between 500 and 487891 " ,Toast.LENGTH_SHORT).show()
                }
            }else if (pageOpen == 2){
                removePageVisibility()
                if(emiDuration!=0) {
                    binding.planLayout.emi.text = emiAmount
                    binding.planLayout.duration.text = emiDuration.toString() + "mo"
                }else{
                    binding.planLayout.emi.text =  binding.emiPage.card1Text.text.toString()
                    binding.planLayout.duration.text = "12mo"
                }
                binding.paymentPage.root.visibility = View.VISIBLE
                binding.amountLayout.root.visibility = View.VISIBLE
                binding.planLayout.root.visibility = View.VISIBLE
                binding.selectionButton.selectionText.text = selectionText3
                val translate = AnimationUtils.loadAnimation(this, R.anim.translation)
                binding.paymentPage.root.startAnimation(translate)
                pageOpen = 3
            }else if(pageOpen == 3){
                removePageVisibility()
                val translate = AnimationUtils.loadAnimation(this, R.anim.translation)
                binding.confirmationPage.root.startAnimation(translate)
                pageOpen = 4
                binding.selectionButton.selectionText.text = selectionText4

                binding.planLayout.dropdownEmi.visibility=View.GONE
                binding.amountLayout.dropdownAmount.visibility=View.GONE
                binding.bankLayout.dropdownBank.visibility=View.GONE

                binding.amountLayout.root.visibility = View.VISIBLE
                binding.planLayout.root.visibility = View.VISIBLE
                binding.bankLayout.root.visibility = View.VISIBLE
                binding.confirmationPage.root.visibility =View.VISIBLE

            }else{
                binding.selectionButton.selectionText.text = selectionText1
                removePageVisibility()
                binding.amountPage.seekBar.progress = 0.0F
                creditValue = 0.0
                binding.amountPage.enterAmount.setText(creditValue.toInt().toString())
                pageOpen=1
                binding.amountPage.root.visibility = View.VISIBLE
                binding.planLayout.dropdownEmi.visibility=View.VISIBLE
                binding.amountLayout.dropdownAmount.visibility=View.VISIBLE
                binding.bankLayout.dropdownBank.visibility=View.VISIBLE

            }
        }

    }
    private fun getData() {
        binding.progressBar.visibility =View.VISIBLE
        binding.progressText.visibility = View.VISIBLE
        binding.progressWarning.visibility = View.VISIBLE
        RetrofitInstance.apiInterface.getData().enqueue(object : Callback<Data?> {
            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                // Fetching Data From API for 1st page (expanded)
                binding.amountPage.firstLineAmountPage.text = response.body()?.items?.get(0)?.open_state?.body?.title.toString()
                binding.amountPage.secondLineAmountPage.text = response.body()?.items?.get(0)?.open_state?.body?.subtitle.toString()
                binding.amountPage.interestRate.text = response.body()?.items?.get(0)?.open_state?.body?.card?.description.toString()
                binding.amountPage.footer.text = response.body()?.items?.get(0)?.open_state?.body?.footer.toString()
                binding.amountPage.creditAmountText.text = response.body()?.items?.get(0)?.open_state?.body?.card?.header
                maxi = response.body()?.items?.get(0)?.open_state?.body?.card?.max_range.toString().toInt()
                mini = response.body()?.items?.get(0)?.open_state?.body?.card?.min_range.toString().toInt()

                // Fetching Data From API for 2nd page (expanded)
                binding.emiPage.firstLineEmiPage.text = response.body()?.items?.get(1)?.open_state?.body?.title.toString()
                binding.emiPage.secondLineEmiPage.text = response.body()?.items?.get(1)?.open_state?.body?.subtitle.toString()
                binding.emiPage.createPlanText.text = response.body()?.items?.get(1)?.open_state?.body?.footer.toString()

                // Fetching Data From API for 3rd page (expanded)
                binding.paymentPage.firstLinePaymentPage.text = response.body()?.items?.get(2)?.open_state?.body?.title.toString()
                binding.paymentPage.secondLinePaymentPage.text = response.body()?.items?.get(2)?.open_state?.body?.subtitle.toString()
                binding.paymentPage.changeAccountText.text = response.body()?.items?.get(2)?.open_state?.body?.footer.toString()
                binding.paymentPage.bankName.text = response.body()?.items?.get(2)?.open_state?.body?.items?.get(0)?.title.toString()
                binding.paymentPage.accountNumber.text = response.body()?.items?.get(2)?.open_state?.body?.items?.get(0)?.subtitle.toString()

                // Fetching Data From API for 1st page (collapsed)
                binding.amountLayout.amountText.text = response.body()?.items?.get(0)?.closed_state?.body?.key1.toString()

                // Fetching Data From API for 2nd page (collapsed)
                binding.planLayout.emiText.text = response.body()?.items?.get(1)?.closed_state?.body?.key1.toString()
                binding.planLayout.durationText.text = response.body()?.items?.get(1)?.closed_state?.body?.key2.toString()

                // Fetching Data From API for 3rd page (collapsed)
                binding.bankLayout.bankAccount.text = response.body()?.items?.get(2)?.open_state?.body?.items?.get(0)?.subtitle.toString()

                //Fetching Data from API for Selection Button
                selectionText1 = response.body()?.items?.get(0)?.cta_text.toString()
                selectionText2 = response.body()?.items?.get(1)?.cta_text.toString()
                selectionText3 = response.body()?.items?.get(2)?.cta_text.toString()

                binding.progressBar.visibility =View.GONE
                binding.progressText.visibility = View.GONE
                binding.progressWarning.visibility = View.GONE
                init()
            }
            override fun onFailure(call: Call<Data?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Unable to Fetch data! Check Internet Connection. Trying Again", Toast.LENGTH_LONG).show()
                getData()
            }
        })
    }
    private fun removePageVisibility() {
        binding.amountLayout.root.visibility = View.GONE
        binding.confirmationPage.root.visibility =View.GONE
        binding.amountPage.root.visibility = View.GONE
        binding.planLayout.root.visibility = View.GONE
        binding.bankLayout.root.visibility = View.GONE
        binding.paymentPage.root.visibility =View.GONE
        binding.emiPage.root.visibility = View.GONE
    }
    private fun init() {
        binding.selectionButton.root.visibility =View.VISIBLE
        binding.helpButton.visibility = View.VISIBLE
        binding.closeButton.visibility = View.VISIBLE
        binding.amountPage.root.visibility =View.VISIBLE
        binding.paymentPage.root.visibility=View.GONE
        binding.emiPage.root.visibility=View.GONE
        binding.confirmationPage.root.visibility =View.GONE
        binding.amountLayout.root.visibility = View.GONE
        binding.planLayout.root.visibility = View.GONE
        binding.bankLayout.root.visibility = View.GONE
        binding.selectionButton.selectionText.text = "Proceed to EMI selection"
        customEmi()
    }
    private fun customEmi() {
        binding.emiPage.customEmi.visibility = View.GONE
        binding.emiPage.customMonth.visibility = View.GONE
        binding.emiPage.customEmiText.visibility = View.GONE
        binding.emiPage.customMonthText2.visibility = View.GONE
        binding.emiPage.goButton.visibility =View.GONE
        binding.emiPage.customMonthText.visibility =View.GONE
        binding.emiPage.customMonth.setText("")
    }
    private fun setEMI(creditValue:Int) {
        val emi12 =round((creditValue * r* (1 + r).pow(12.0))/((1 + r).pow(12.0) -1))
        val emi9=round((creditValue * r* (1 + r).pow(9.0))/((1 + r).pow(9.0) -1))
        val emi6 =round((creditValue * r* (1 + r).pow(6.0))/((1 + r).pow(6.0) -1))
        val emi3 = round((creditValue * r* (1 + r).pow(3.0)) /((1 + r).pow(3.0) -1))
        binding.emiPage.card1Text.text = "₹"+emi12.toString()
        binding.emiPage.card2Text.text = "₹"+emi9.toString()
        binding.emiPage.card3Text.text = "₹"+emi6.toString()
        binding.emiPage.card4Text.text = "₹"+emi3.toString()
    }
    override fun onBackPressed() {

        if(pageOpen == 1) {
            super.onBackPressed()
        }else if(pageOpen == 2){
            removePageVisibility()
            binding.amountPage.root.visibility = View.VISIBLE
            val translate = AnimationUtils.loadAnimation(this, R.anim.translation)
            binding.amountPage.root.startAnimation(translate)
            pageOpen = 1
            binding.selectionButton.selectionText.text = selectionText1
        }else if(pageOpen == 3){
            removePageVisibility()
            binding.emiPage.root.visibility = View.VISIBLE
            val translate = AnimationUtils.loadAnimation(this, R.anim.translation)
            binding.emiPage.root.startAnimation(translate)
            pageOpen = 2
            binding.amountLayout.root.visibility = View.VISIBLE

            binding.selectionButton.selectionText.text = selectionText2
        }else{
            removePageVisibility()
            binding.selectionButton.selectionText.text = selectionText1
            binding.amountPage.enterAmount.setText("0")
            binding.amountPage.root.visibility = View.VISIBLE
            pageOpen=1
            binding.planLayout.dropdownEmi.visibility=View.VISIBLE
            binding.amountLayout.dropdownAmount.visibility=View.VISIBLE
            binding.bankLayout.dropdownBank.visibility=View.VISIBLE
        }
    }
    private fun emi() {
        binding.emiPage.card1Tick.visibility = View.GONE
        binding.emiPage.card2Tick.visibility = View.GONE
        binding.emiPage.card3Tick.visibility = View.GONE
        binding.emiPage.card4Tick.visibility = View.GONE
    }
}