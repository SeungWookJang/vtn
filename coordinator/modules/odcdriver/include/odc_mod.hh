/*
 * Copyright (c) 2013 NEC Corporation
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 */

#ifndef _ODC_MOD_HH__
#define _ODC_MOD_HH__

#include <rest_client.hh>
#include <odc_vtn.hh>
#include <odc_vbr.hh>
#include <odc_vbrif.hh>
#include <odc_driver_common_defs.hh>
#include <vtn_drv_module.hh>
#include <arpa/inet.h>
#include <unc/tc/external/tc_services.h>
#include <string>

namespace unc {
namespace odcdriver {

class ODCModule: public pfc::core::Module, public unc::driver::driver {
 public:
  /**
   * @brief     - Paramaretrised Constructor
   * @param[in] - pfc_modattr obj
   */
  explicit ODCModule(const pfc_modattr_t*& obj)
      : Module(obj),
        ping_interval(0) { }
  /**
   * @brief     - Gets the controller type
   * @param[in] - unc_keytype_ctrtype_t enum
   * @return    - returns Enum of type unc_keytype_ctrtype_t
   */
  unc_keytype_ctrtype_t get_controller_type();

  /**
   * @brief  - init method - Get Instance of vtndrvinntf & Register into RegisterDriver
   * @return - returns PFC_TRUE on success
   *         returns  PFC_FALSE on failure
   */
  pfc_bool_t init();

  /**
   * @brief  - Fini
   * @return - returns PFC_TRUE on success/
   *         returns PFC_FALSE on failure
   */
  pfc_bool_t fini();

  /**
   * @brief   - Is 2phase commit supported or not
   * @return  - returns PFC_TRUE if 2phase commit support is needed
   *            returns PFC_FALSE if 2 phase commit is not required.
   */
  pfc_bool_t is_2ph_commit_support_needed();

  /**
   * @brief  - Is audit collection needed or not
   * @return - returns PFC_TRUE if audit collection is required/returns PFC_FALSE
   *           if audit collection is not required.
   */
  pfc_bool_t is_audit_collection_needed();

  /**
   * @brief             - Creates Controller pointer with specific values and return
   * @param[in] key_ctr - Controller key structure
   * @param[in] val_ctr - Controller value structute
   * @return            - returns new added controller pointer
   */
  unc::driver::controller* add_controller(const key_ctr_t& key_ctr,
                                          const val_ctr_t& val_ctr);

  /**
   * @brief               - Updates Controller pointer with specific values and return
   * @param[in] key_ctr   - Controller key structure
   * @param[in] val_ctr   - Controller value structute
   * @param[out] ctrl_inst- Controller pointer
   * @return              - returns PFC_TRUE
   */
  pfc_bool_t update_controller(const key_ctr_t& key_ctr,
                               const val_ctr_t& val_ctr,
                               unc::driver::controller* ctrl_inst);

  /**
   * @brief                 - Deletes Controller pointer with specific values
   * @param[in] delete_inst - Controller pointer
   * @return                - returns PFC_TRUE if the controller is deleted/ returns PFC_FALSE
   *                          if controller is not deleted
   */
  pfc_bool_t delete_controller(unc::driver::controller* delete_inst);

  /**
   * @brief                    - Gets the driver command as per the keytype
   * @param[in] key_type       - unc Keytype
   * @return  driver_command*  - returns corresponding instance driver_command* of the
   *                             key type
   */
  unc::driver::driver_command* create_driver_command(unc_key_type_t key_type);

  /**
   * @brief     -  HandleVote
   * @param[in] -  Controller pointer
   * @return    -  returns unc::tclib::TcCommonRet - enum value
   */
  unc::tclib::TcCommonRet HandleVote(unc::driver::controller*);

  /**
   * @brief     - Handle the commit
   * @param[in] - Controller pointer
   * @return    -  returns unc::tclib::TcCommonRet - enum value
   */
  unc::tclib::TcCommonRet HandleCommit(unc::driver::controller*);

  /**
   * @brief     - Handle the abort
   * @param[in] - Controller pointer
   * @return    - returns unc::tclib::TcCommonRet - enum value
   */
  unc::tclib::TcCommonRet HandleAbort(unc::driver::controller*);

  /**
   * @brief   - Ping needed for the ODC Conttoller or not
   * @return  - returns PFC_TRUE if ping is need / returns PFC_FALSE
   *            if ping is not needed.
   */
  pfc_bool_t is_ping_needed();

  /**
   * @brief  - Gets the ping interval
   * @return - returns the ping interval
   */
  uint32_t get_ping_interval();

  /**
   * @brief  - Gets the ping interval count
   * @return - returns the ping interval count
   */
  uint32_t get_ping_fail_retry_count();

  /**
   * @brief     - ping controller available or not
   * @param[in] - Controller pointer
   * @return    - returns PFC_TRUE if controller is available/
   *              returns PFC_FALSE if controller is not available
   */
  pfc_bool_t ping_controller(unc::driver::controller*);

 private:
  /**
   * @brief     - Read configuration file of odcdriver
   * @param[in] - None
   * @return    - None
   */
  void read_conf_file();

  /**
   * @brief     - Notify Audit to TC
   * @param[in] - controller id
   * @return    - returns ODC_DRV_SUCCESS on sending start
   *              notification to tc
   */
  uint32_t notify_audit_start_to_tc(std::string controller_id);

  /**
   * @brief     - reads odc_port, connection_timeout, request time out values form
   *              conf file else take default vlaues
   * @param[out] - odc_port in uint32_t
   * @param[out] - connection_time_out in seconds
   * @param[out] - request time out in seconds
   */
  void read_conf_file(uint32_t &odc_port,
                      uint32_t &connection_time_out,
                      uint32_t &request_time_out);

  /**
   * @brief      - reads user name password from conf file else take default
   *               values
   * @param[out]  - username in string
   * @param[out]  - password in string
   */
  void read_user_name_password(std::string &user_name, std::string &password);

  /**
   * @brief      - gets user name password from controller pointer else read
   *               conf file
   * @param[out]  - username in string
   * @param[out]  - password in string
   */
  void get_username_password(unc::driver::controller* ctr_ptr,
                             std::string &user_name, std::string &password);

 private:
  uint32_t ping_interval;  // in seconds
};
}  //  namespace odcdriver
}  //  namespace unc
#endif
