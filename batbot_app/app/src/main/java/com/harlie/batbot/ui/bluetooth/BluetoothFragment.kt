// Copyright (c) 2019, Lee Hounshell. All rights reserved.

package com.harlie.batbot.ui.bluetooth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harlie.batbot.ControlActivity
import com.harlie.batbot.ControlActivity.Companion.EXTRA_ADDRESS
import com.harlie.batbot.ControlActivity.Companion.EXTRA_DEVICE
import com.harlie.batbot.ControlActivity.Companion.EXTRA_NAME
import com.harlie.batbot.R
import com.harlie.batbot.databinding.BluetoothFragmentBinding
import com.harlie.batbot.model.BluetoothDeviceModel
import com.harlie.batbot.util.BluetoothRecyclerAdapter


class BluetoothFragment : Fragment() {
    val TAG = "LEE: <" + BluetoothFragment::class.java.getName() + ">"

    private lateinit var m_selectedDevice: BluetoothDeviceModel
    private lateinit var m_View: View
    private lateinit var m_BluetoothFragmentBinding: BluetoothFragmentBinding
    private lateinit var m_BluetoothViewModel: Bluetooth_ViewModel
    private lateinit var m_RecyclerView: RecyclerView
    private lateinit var m_BluetoothRecyclerAdapter: BluetoothRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        m_BluetoothFragmentBinding = DataBindingUtil.inflate(
            inflater, com.harlie.batbot.R.layout.bluetooth_fragment, container, false
        )
        m_RecyclerView = m_BluetoothFragmentBinding.recyclerView
        m_BluetoothFragmentBinding.lifecycleOwner = viewLifecycleOwner
        m_View = m_BluetoothFragmentBinding.getRoot()
        return m_View
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            m_BluetoothViewModel = ViewModelProviders.of(it).get(Bluetooth_ViewModel::class.java)
        }

        m_BluetoothRecyclerAdapter = BluetoothRecyclerAdapter(m_BluetoothViewModel)
        m_RecyclerView.adapter = m_BluetoothRecyclerAdapter
        m_RecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        m_BluetoothFragmentBinding.selected = false
        m_BluetoothFragmentBinding.btFragment = this
        m_BluetoothFragmentBinding.recyclerView.adapter = m_BluetoothRecyclerAdapter
        m_BluetoothFragmentBinding.executePendingBindings()

        m_BluetoothFragmentBinding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        m_BluetoothViewModel.getBluetoothDevicesList().observe(this, Observer {
            Log.d(TAG, "observe: getBluetoothDevicesList() content: it=" + it)
            m_BluetoothRecyclerAdapter.m_deviceCache = it
            m_BluetoothRecyclerAdapter.notifyDataSetChanged()
        })

        m_BluetoothViewModel.getSelectedDevice().observe(this, Observer {
            Log.d(TAG, "observe: getSelectedDevice()=" + it)
            m_selectedDevice = it
            m_BluetoothFragmentBinding.selected = true
        })
    }

    fun onClickRefresh(v: View) {
        Log.d(TAG, "onClickRefresh")
        v.playSoundEffect(android.view.SoundEffectConstants.CLICK)
        m_BluetoothFragmentBinding.selected = false
        m_BluetoothViewModel.initializeDeviceList(context!!)
    }

    fun onClickConnect(v: View) {
        Log.d(TAG, "onClickConnect")
        v.playSoundEffect(android.view.SoundEffectConstants.CLICK)
        // Cancel discovery because it otherwise slows down the connection.
        m_BluetoothViewModel.cancelDiscovery()
        gotoControlActivity(m_selectedDevice)
    }

    fun gotoControlActivity(btModel: BluetoothDeviceModel) {
        Log.d(TAG, "gotoControlActivity: btModel name=" + btModel.device.name + ", address=" + btModel.device.address)
        val controlIntent: Intent = Intent(context, ControlActivity::class.java)
        controlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        controlIntent.putExtra(EXTRA_NAME, btModel.device.name)
        controlIntent.putExtra(EXTRA_ADDRESS, btModel.device.address)
        controlIntent.putExtra(EXTRA_DEVICE, btModel.device)
        startActivity(controlIntent)
        activity!!.overridePendingTransition(0, R.anim.fade_out)
        activity!!.finish()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

}
