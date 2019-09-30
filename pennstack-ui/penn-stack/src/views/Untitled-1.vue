<template>
  <section class="conversation verificationResponse">
    <m-loading :isLoading="isLoading" />
    <m-loading :isLoading="isNcicLoading" />
    <m-error-lists
      :service-errors="serviceErrorMessages"
      :warnings="warningErrorMessages"
      :notices="infoErrorMessages"
    />

    <div class="column">
      <h2 class="titleHeaderText">NMVTIS Verification Response Summary</h2>
    </div>

    <div class="column">
      <m-card title="Reference Information">
        <template slot="content">
          <div class="media-content">
            <div class="row">
              <div class="column">
                <div class="row">
                  <m-label :label="$t('label.common.patitlenumber')" bold="true"></m-label>
                </div>
                <span v-if="title.titleNum" class="ml-5p">{{ title.titleNum }}</span>
                <span v-if="title.titleMod11" class="ml-5p">{{ title.titleMod11 }}</span>
              </div>
              <div class="column">
                <div class="row">
                  <m-label :label="$t('label.vehicle.vin')" bold="true"></m-label>
                </div>
                <div class="ml-5p row">{{ vehicle.vinNum}}</div>
              </div>
              <div class="column">
                <div class="row">
                  <m-label
                    :label="$t('label.registration.reissueRegistrationHeader.lastIssuedDate')"
                    bold="true"
                  />
                </div>
                <div class="row ml-5p">{{ formatedDate(this.title.titleIssueDate) }}</div>
              </div>
              <div class="column">
                <div class="row">
                  <m-label :label="$t('label.common.priorstate')" bold="true"></m-label>
                </div>
                <div class="row ml-5p">{{ inquiry.priorState }}</div>
              </div>
            </div>
            <div>
              <m-caratsName
                :owner="this.contextInfo.titleOwnerBkp"
                :isEditable="false"
                custType="oCaratsName"
                ref="mCaratsName"
              />
            </div>
          </div>
        </template>
      </m-card>
    </div>

    <!-- Vehicle Info Card -->
    <div class="column">
      <m-card title="Vehicle Information">
        <template slot="content">
          <div class="media-content" style="padding-left: 12px;">
            <div class="row content columns">
              <div class="column is-whole">
                <div class="display-grid">
                  <div class="display-grid">
                    <div class="row">
                      <m-label :label="$t('label.common.message')"></m-label>
                    </div>
                    <span class="row ml-5p">{{vehicleMessage}}</span>
                  </div>
                </div>
              </div>
            </div>

            <div class="row content columns">
              <div class="column is-3">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.common.currentSOT')" bold="true"></m-label>
                  </div>
                  <div class="row ml-5p">{{ currentSOT }}</div>
                </div>
              </div>
              <div class="column is-1"></div>
              <div class="column is-3">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.common.currentSOTNumber')" bold="true"></m-label>
                  </div>
                  <div class="row ml-5p">{{currentSOTNumber}}</div>
                </div>
              </div>
              <div class="column is-1"></div>
              <div class="column">
                <m-autocomplete-search
                  customClass="is-required"
                  id="documentationVerified"
                  name="DocumentationVerified"
                  :label="$t('label.common.documentationVerified')"
                  refTableType="MV01-NMVITS-DOC-OPTIONS"
                  filterColumn="FGAC-FILTER"
                  v-model="contextInfo.documentVerification"
                  @input="validateDocVerified"
                  validate="required"
                  :recordsSkipped="filterDocsVerifiedBasedOnFGAC('MV01.NMVITS.DOCOPTIONS')"
                />
              </div>
            </div>

            <div class="row content columns">
              <div class="column is-3">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.vehicle.make')" bold="true"></m-label>
                  </div>
                  <div class="row ml-5p">{{ vehicleMake}}</div>
                </div>
              </div>
             <div class="column is-1"></div>
              <div class="column is-3">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.vehicle.modelyear')" bold="true"></m-label>
                 </div>
                  <div class="row ml-5p">{{ vehicleModelYear }}</div>
                </div>
              </div>
              <div class="column is-1"></div>
              <div class="column">
                <m-numeric
                  name="ValueOfVehicle"
                  :label="$t('label.common.ValueOfVehicle')"
                  :max="10"
                  icon="fa fa-dollar-sign"
                  v-model="contextInfo.vehValue"
                  :disabled="disableOnDocVerifiedInd||disableOnDocVerifiedIndForMv06"
                  :validate="valueRequired"
                  v-on:input="removeErrorOnKey"
                  class="float-none"
                />
              </div>
           </div>

            <div class="row content columns">
              <div class="column is-3">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.nmvtis.vinPointerOdometerReading')" bold="true"></m-label>
                  </div>
                  <div class="row">{{ this.formatValue(vinPointerOdometer) }}</div>
                </div>
              </div>
              <div class="column is-1"></div>
              <div class="column is-2">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.nmvtis.horsePower')" bold="true"></m-label>
                  </div>
                  <div class="row">{{ this.formatValue(contextInfo.vehHorsePower) }}</div>
                </div>
              </div>
              <div class="column is-2">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.nmvtis.gvw')" bold="true"></m-label>
                  </div>
                  <div class="row">{{this.formatValue(grossVehWght)}}</div>
                </div>
              </div>
              <div class="column">
                <m-numeric
                  name="CostOfRepairs"
                  :label="$t('label.common.costOfRepairs')"
                  :max="10"
                  icon="fa fa-dollar-sign"
                  v-model="contextInfo.vehCostOfRepairs"
                  class="float-none"
                  :disabled="disableOnDocVerifiedInd||disableOnDocVerifiedIndForMv06"
                  :validate="valueRequired"
                  v-on:input="removeErrorOnKey"
                />
              </div>
            </div>
          </div>
        </template>
      </m-card>
    </div>

    <!-- Brand Info Card -->
    <div class="column">
      <m-card title="Brand Information">
        <template slot="content">
          <div class="media-content">
            <div class="column">
              <div class="row">
                <m-label :label="$t('label.nmvtis.brandMessage')" bold="true"></m-label>
              </div>
              <div class="row">
                <span class="ml-5p">{{brandMessage}}</span>
              </div>
            </div>
            <div class="box" v-show="brandData.length" style="margin:0.5rem 1rem">
              <m-datatable
                v-show="total"
                :columns="columns"
                :data="brandData"
                :addRow="false"
                scroll="clip"
                :total="total"
              />
            </div>
          </div>
        </template>
      </m-card>
    </div>

    <!-- State Info Card -->
    <div class="column">
      <m-card title="State & Additional Information">
        <template slot="content">
          <div class="media-content">
            <div class="row">
              <div class="column is-whole">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.nmvtis.stateMessage')" bold="true"></m-label>
                  </div>
                  <div class="row">
                    <span class="ml-5p">{{stateMessage}}</span>
                  </div>
                </div>
              </div>
              <div class="column is-whole">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.nmvtis.additionalInformationMessage')" bold="true"></m-label>
                  </div>
                  <div class="row">
                    <span class="ml-5p">{{additionalMessage}}</span>
                  </div>
                </div>
              </div>
              <div class="column is-whole">
                <div class="display-grid">
                  <div class="row">
                    <m-label :label="$t('label.nmvtis.sotOdometerReading')" bold="true"></m-label>
                  </div>
                  <div class="row ml-5p">{{ stateOdometerReading }}</div>
                </div>
              </div>
            </div>
          </div>
        </template>
      </m-card>
    </div>

    <!--Footer Buttons Section-->
    <m-required-field />
    <div class="footerButtons">
      <m-custom-button
        custom-class="button is-primary button-size mr-2"
        class="float-right"
        :label="$t('label.button.next')"
        :help-text="$t('shortcuts.nextPage')"
        :action="onNextClick"
        :disabled="false"
      />
      <m-custom-button
        custom-class="button button-size button-border-navy ml-0"
        class="float-left"
        :help-text="$t('shortcuts.previousPage')"
        :label="$t('label.button.back')"
        :action="onBackClick"
      />
      <m-button
        class="button button-size button-border-navy ml-2"
        :label="$t('label.button.exit')"
        @click.native="isExitModalActive = true"
      />
    </div>
    <br />
    <m-modal
      modal-title="Warning Message"
      :isModalActive="isMStopModal"
      @isModalChanged="isMStopModal = false"
      scroll="clip"
    >
      <template slot="modal-body">
        <div class="columns is-gapless editModalItems">
          <p>
            <strong>The Title record has an "M" stop - Mobile Home/Real Estate Cancellation. Do you want to remove the "M" stop and continue the title transaction?"</strong>
          </p>
        </div>
        <br />
        <br />
        <div class="footerButtons">
          <m-button
            class="button button-size button-border-navy float-left"
            :label="$t('label.common.no')"
            @click.native="onMStopNo()"
          ></m-button>
          <m-button
            class="button is-primary button-size float-right"
            :label="$t('label.common.yes')"
            @click.native="onMStopYes()"
          ></m-button>
        </div>
      </template>
    </m-modal>
    <m-exit-modal :active.sync="isExitModalActive" @close="isExitModalActive = false" />
    <m-modal-wrapper
      :active.sync="isNavigateToPrompt"
      @close="val => isNavigateToPrompt = val"
      @onConfirmingYes="navigateBackPrompt"
    />
  </section>
</template>



<script>
import { createHelpers } from "vuex-map-fields";
import { httpUtil } from "@/api/httpUtil";
import MutationTypes from "@/store/MutationTypes";
import CONSTANTS from "@/constants/TitleConstants";
import feeCalcMixin from "@/mixins/feeCalcMixin";
import CommonFunctionsMixin from "@/mixins/CommonFunctionsMixin";
import commonBRUMixin from "@/mixins/CommonBRUsMixin";
import ProRateCalcMixin from "@/mixins/ProRateCalcMixin";
import { CONVERSATION_UTILS } from "@/pages/common/ConversationUtils";
import { FGAC_RULES } from "@/pages/common/FGACRules";
import { CommonObjectsMixin } from "@/mixins/CommonObjectsMixin";
const { mapFields } = createHelpers({
  getterType: "nmvtis/getField",
  mutationType: "nmvtis/updateField"
});

export default {
  data() {
    return {
      pageName: "NMVTIS",
      isNavigateToPrompt: false,
      nmvtisVerificationFlag: false,
      SOR: this.$store.state.SOR,
      nmvtisForSuspenseWidInd: false,
      nmvtisForTitleForceInd: false,
      nmvtisForTfAndWidInd: false,
      nmvtisForWidAndMcoInd: false,
      nmvtisForVinAndMcoInd: false,
      nmvtisForWidAndExistingPaRrd: false,
      nmvtisForVinAndOutOfState: false,
      nmvtisForWidOutOfState: false,
      nmvtisWidForExistingPaRec: false,
      nmvtisCallInd: false,
      disableOnDocVerifiedInd: false,
      valueRequired: "",
      isExitModalActive: false,
      isLoading: false,
      isNcicLoading: false,
      fullBrandList: [],
      isMStopModal: false,
      noticeMessages: [],
      warningMessages: [],
      nmvtisForPriorStateAndTitle: false,
      disableOnDocVerifiedIndForMv06: false,
      brandCodes: [],
      total: 0,
      noNmvtisCallInd: false,
      receivedNmvtisData: false,
      receivedNcicData: false,
      newOwnerObject: {},
      nmvtisPaBrandMapping: [],
      ncicRetryCount: CONSTANTS.NCIC_RETRY_COUNT,
      ncicWaitInterval: CONSTANTS.NCIC_WAIT_INTERVAL,
      nmvtisRetryCount: CONSTANTS.NMVTIS_RETRY_COUNT,
      nmvtisWaitInterval: CONSTANTS.NMVTIS_WAIT_INTERVAL,
      documentVerified: [
        { id: "Y", val: "Yes" },
        { id: "N", val: "No" },
        { id: "A", val: "Appraisal" },
        { id: "O", val: "Override" }
      ],
      columns: {
        values: [
          {
            field: "brand",
            label: "Brand",
            disabled: true,
            path: ""
          },
          {
            field: "stateApplied",
            label: "State Applied",
            disabled: true,
            path: ""
          },
          {
            field: "dateApplied",
            label: "Date Applied",
            disabled: true,
            path: ""
          }
        ]
      },
      vehicleStopcodes: {
        A: 5006,
        B: 5002,
        D: 5066,
        E: 5004,
       F: 5013,
        G: 5011,
        H: 5005,
        I: 5016,
        J: 5080,
        K: 5009,
        L: 5026,
        M: 5007,
        N: 5029,
        O: 5008,
        P: 1307,
        Q: 5021,
        S: 5010,
        T: 5020,
        V: 5022,
        W: 5023,
        X: 5999,
        Y: 5999,
        Z: 5025
      },
      tempTransCd: [],
      feeCalculationFlag: false,
      termInMonths: null,
      proRateAmountA: null,
      proRateAmountB: null,
      registrationFeeAmount: null,
      requestBody: {
        refTypeTxt: null,
        refCode: "",
        refCodeDesc: "",
        filterColumn: null,
        pageNum: 1,
        skippedRecords: null,
        refAttrval: null,
        displayedRecords: null,
        filterPlate: false,
        lowerBound: null,
        upperBound: null
      }
    };
  },
  mixins: [
    feeCalcMixin,
    CommonFunctionsMixin,
    commonBRUMixin,
    ProRateCalcMixin,
    CommonObjectsMixin
  ],
  computed: {
    ...mapFields({
      title: "nmvtis.snapshots[0].title",
      owner: "nmvtis.snapshots[0].title.owner",
      vehicle: "nmvtis.snapshots[0].vehicle",
      inquiry: "inquiry",
      contextInfo: "nmvtis.snapshots[0].contextInfo",
      registration: "nmvtis.snapshots[0].registration",
      vinHist: "nmvtis.snapshots[0].vinHistory",
      transferInquiry: "transferInquiry",
      fromPlate: "nmvtis.snapshots[0].fromSnapshot.plate",
      fromTitle: "nmvtis.snapshots[0].fromSnapshot.title",
      fromRegistration: "nmvtis.snapshots[0].fromSnapshot.registration",
      nmvtisResponse: "nmvtis.snapshots[0].contextInfo.nmvtisResponse",
      plate: "nmvtis.snapshots[0].plate",
      nmvtis: "nmvtis",
      feeDetails: "nmvtis.snapshots[0].feeDetails",
      wids: "nmvtis.snapshots[0].wids"
    }),

    vehicleMake: function() {
      if (this.nmvtisResponse && this.nmvtisResponse.response) {
        if (
          this.nmvtisResponse.response.vehicleSummary &&
          this.nmvtisResponse.response.vehicleSummary.vehicleMakeCode
        ) {
          return this.nmvtisResponse.response.vehicleSummary.vehicleMakeCode;
        } else if (
          this.nmvtisResponse.response.vehicleNoTitleHistory &&
          this.nmvtisResponse.response.vehicleNoTitleHistory.length == 1 &&
          this.nmvtisResponse.response.vehicleNoTitleHistory[0].vehicleMakeCode
        ) {
          return this.nmvtisResponse.response.vehicleNoTitleHistory[0]
            .vehicleMakeCode;
        } else if (
          this.nmvtisResponse.response.jurisdictionVehicle &&
          this.nmvtisResponse.response.jurisdictionVehicle.length == 1 &&
          this.nmvtisResponse.response.jurisdictionVehicle[0].vehicleModelCode
        ) {
          return this.nmvtisResponse.response.jurisdictionVehicle[0]
            .vehicleModelCode;
        } else if (
          this.nmvtisResponse.response.vehicleDesignation &&
          this.nmvtisResponse.response.vehicleDesignation.length > 0 &&
          this.nmvtisResponse.response.vehicleDesignation[0].vehicleMakeCode
        ) {
          return this.nmvtisResponse.response.vehicleDesignation[0]
            .vehicleMakeCode;
        }
      }
    },
    vehicleModelYear: function() {
      if (this.nmvtisResponse && this.nmvtisResponse.response) {
        if (
          this.nmvtisResponse.response.vehicleSummary &&
          this.nmvtisResponse.response.vehicleSummary.itemModelYearDate
        ) {
          return this.nmvtisResponse.response.vehicleSummary.itemModelYearDate;
        } else if (
          this.nmvtisResponse.response.vehicleNoTitleHistory &&
          this.nmvtisResponse.response.vehicleNoTitleHistory.length == 1 &&
          this.nmvtisResponse.response.vehicleNoTitleHistory[0]
            .itemModelYearDate
        ) {
          return this.nmvtisResponse.response.vehicleNoTitleHistory[0]
            .itemModelYearDate;
        } else if (
          this.nmvtisResponse.response.jurisdictionVehicle &&
          this.nmvtisResponse.response.jurisdictionVehicle.length == 1 &&
          this.nmvtisResponse.response.jurisdictionVehicle[0].itemModelYearDate
        ) {
          return this.nmvtisResponse.response.jurisdictionVehicle[0]
            .itemModelYearDate;
        } else if (
          this.nmvtisResponse.response.vehicleDesignation &&
          this.nmvtisResponse.response.vehicleDesignation.length > 0 &&
          this.nmvtisResponse.response.vehicleDesignation[0].itemModelYearDate
        ) {
          return this.nmvtisResponse.response.vehicleDesignation[0]
            .itemModelYearDate;
        }
      }
    },
    vinPointerOdometer: function() {
      let vinPointerOdometer = "",
        statePointerOdometer = "";
      if (this.nmvtisResponse && this.nmvtisResponse.response) {
        if (
          this.nmvtisResponse.response.vehicleNoTitleHistory &&
          this.nmvtisResponse.response.vehicleNoTitleHistory.length == 1 &&
          this.nmvtisResponse.response.vehicleNoTitleHistory[0]
            .vehicleOdometerReadingMeasure
        ) {
          vinPointerOdometer = this.nmvtisResponse.response
            .vehicleNoTitleHistory[0].vehicleOdometerReadingMeasure;
        }

        if (
          this.nmvtisResponse.response.jurisdictionVehicle &&
          this.nmvtisResponse.response.jurisdictionVehicle.length == 1 &&
          this.nmvtisResponse.response.jurisdictionVehicle[0]
            .vehicleOdometerReadingMeasure
        ) {
          statePointerOdometer = this.nmvtisResponse.response
            .jurisdictionVehicle[0].vehicleOdometerReadingMeasure;
        }

        if (parseInt(vinPointerOdometer) > parseInt(statePointerOdometer)) {
          return vinPointerOdometer;
        } else {
          return statePointerOdometer;
        }
      }
    },
    currentSOT: function() {
      if (
        this.nmvtisResponse &&
        this.nmvtisResponse.response.jurisdictionVehicle &&
        this.nmvtisResponse.response.jurisdictionVehicle.length == 1
      ) {
        return this.nmvtisResponse.response.jurisdictionVehicle[0]
          .titleIssuingAuthorityName;
      }
    },
    currentSOTNumber: function() {
      if (
        this.nmvtisResponse &&
        this.nmvtisResponse.response.jurisdictionVehicle &&
        this.nmvtisResponse.response.jurisdictionVehicle.length == 1
      ) {
        return this.nmvtisResponse.response.jurisdictionVehicle[0].titleID;
      }
    },
    grossVehWght: function() {
      if (
        this.nmvtisResponse &&
        this.nmvtisResponse.response.jurisdictionVehicle &&
        this.nmvtisResponse.response.jurisdictionVehicle.length == 1
      ) {
        return this.nmvtisResponse.response.jurisdictionVehicle[0]
          .vehicleGrossLadenSumWeightMeasure;
      }
    },
    stateOdometerReading: function() {
      if (
        this.nmvtisResponse &&
        this.nmvtisResponse.response &&
        this.nmvtisResponse.response.jurisdictionVehicle &&
        this.nmvtisResponse.response.jurisdictionVehicle.length == 1 &&
        this.nmvtisResponse.response.jurisdictionVehicle[0]
          .vehicleOdometerReadingMeasure
      ) {
        return this.nmvtisResponse.response.jurisdictionVehicle[0]
          .vehicleOdometerReadingMeasure;
      }
    },
    brandData: function() {
      let brandList = [];
      let brandName = "";

      if (
        this.nmvtisResponse &&
        this.nmvtisResponse.response &&
        this.nmvtisPaBrandMapping &&
        this.nmvtisPaBrandMapping.length > 0 &&
        this.nmvtisResponse.response.vehicleDesignation &&
        this.nmvtisResponse.response.vehicleDesignation.length > 0
      ) {
        for (
          var i = 0;
          i < this.nmvtisResponse.response.vehicleDesignation.length;
          i++
        ) {
          var item = this.nmvtisResponse.response.vehicleDesignation[i];

          for (var j = 0; j < this.nmvtisPaBrandMapping.length; j++) {
            if (
              item.vehicleBrandCode ==
              this.nmvtisPaBrandMapping[j].referenceCode
            ) {
              if (
                this.nmvtisPaBrandMapping[j].addlAttrName ==
                "MV0623-CARATS-BRAND-DESC"
              ) {
                brandName = this.nmvtisPaBrandMapping[j].referenceCodeDesc;
              }
            }
          }
          if (brandName) {
            brandList.push({
              brand: brandName,
              dateApplied: item.vehicleBrandDate,
              stateApplied: item.reportingEntityName
            });
          }

          if (item.vehicleBrandCode) {
            this.fullBrandList.push({
              brand: item.vehicleBrandCode,
              dateApplied: moment(item.vehicleBrandDate).format("YYYY-MM-DD"),
              stateApplied: item.reportingEntityName
            });
          }
        }
        brandList.sort(
          (a, b) => new Date(b.dateApplied) - new Date(a.dateApplied)
        );
        this.total = brandList.length;

        if (this.fullBrandList && this.fullBrandList.length > 0) {
          this.fullBrandList.sort(
            (a, b) => new Date(a.dateApplied) - new Date(b.dateApplied)
          );
        }
      }

      return brandList;
    },
    brandMessage: function() {
      var brandMsg = "";
      if (this.nmvtisResponse && this.nmvtisResponse.response) {
        brandMsg = this.nmvtisResponse.response.brandMessage;
        if (
          this.contextInfo.additionalBrandMessages &&
          this.contextInfo.additionalBrandMessages.length > 0
        ) {
          for (
            let i = 0;
           i < this.contextInfo.additionalBrandMessages.length;
            i++
          ) {
            brandMsg =
              brandMsg + " " + this.contextInfo.additionalBrandMessages[i];
          }
        }
      }

      return brandMsg;
    },
    stateMessage: function() {
      if (this.nmvtisResponse && this.nmvtisResponse.response) {
        return this.nmvtisResponse.response.stateMessage;
      }
    },
    vehicleMessage: function() {
      var vehicleMsg = "";
      if (this.nmvtisResponse && this.nmvtisResponse.response) {
        vehicleMsg = this.nmvtisResponse.response.vehicleMessage;
        if (
          this.nmvtisResponse.response.vehicleNoTitleHistory &&
          this.nmvtisResponse.response.vehicleNoTitleHistory.length == 1 &&
          CONSTANTS.CSOT_IN_PROGRESS.includes(
            this.nmvtisResponse.response.vehicleNoTitleHistory[0]
              .nmvtisChangeStateOfTitleCode
          )
        ) {
          vehicleMsg = vehicleMsg + " " + CONSTANTS.CSOT_VEH_BRAND_MESSAGE;
        }
      }

      return vehicleMsg;
    },
    additionalMessage: function() {
      if (this.nmvtisResponse && this.nmvtisResponse.response) {
        return (
          this.nmvtisResponse.response.additionalMessage +
          (this.contextInfo.ncicMessage ? this.contextInfo.ncicMessage : "")
        );
      } else if (this.contextInfo.ncicMessage) {
        return this.contextInfo.ncicMessage;
      }
    }
  },
  created: function() {
    if(this.contextInfo.fromTitlePrompt == true|| 
    this.contextInfo.fromUpdateVehicle==true ||
    this.contextInfo.fromSalvagePrompt == true){
     this.contextInfo.titleOwnerBkp = this.$_.cloneDeep(this.title.owner);
    }
  },
  mounted: function() {
    // Page Load validations
    let validationErrs = this.$store.state["nmvtis"].nmvtis.snapshots[0]
      .validation
      ? this.$store.state["nmvtis"].nmvtis.snapshots[0].validation
          .validationErrors
      : [];
    if (document.getElementById("view-container-id").querySelector("input")) {
      document
        .getElementById("view-container-id")
        .querySelectorAll("input:enabled")[0]
        .focus();
    }
    if (
      this.contextInfo.promtInfoMessages &&
      this.contextInfo.promtInfoMessages.length
    ) {
      this.addPageError(this.contextInfo.promtInfoMessages);
    }
    this.addError(validationErrs, true);
    this.genrateWarningMessages(
      this.$_.filter(validationErrs, e => {
        return e.severity === "WARNING" || e.severity === "INFO";
      })
    );
    this.setNmvtisNcicRetryCounts();
    console.log("On Mounted log 1 setNmvtisNcicRetryCounts");
    this.validateMv01OnLoadBRUs();
    this.validateMv06OnLoadBRUs();
    this.validateMv11OnLoadBRUs();
    this.validateCommonOnLoadBRUs();
  },

  methods: {
    handleKeyPress(e) {
      if (this.isMvdlsModalActive) return;
      if (e.ctrlKey && e.keyCode === 39) {
        this.onNextClick();
      } else if (e.ctrlKey && e.keyCode === 37) {
        this.onBackClick();
        e.preventDefault();
      }
    },
    onBackClick() {
      if (this.$store.state.conversationType == "06") {
        this.$router.push("/update_vehicle/change_vehicle");
      } else {
        this.isNavigateToPrompt = true;
      }
    },
    navigateBackPrompt() {
      let routeName = "";
      let conv = this.$root.currentConversation;
      if (conv.includes("_")) {
        conv = conv.replace(/_/g, "-");
        routeName = conv + "-prompt";
      }
      this.$router.push({ name: routeName });
    },
    addError(errors, fromBackend = false) {
      this.$errorList.addSystemErrors(errors, this.pageName, fromBackend);
      if (
        errors &&
        CONSTANTS.NMVTIS_USED_VEH_INQUIRY_GEN_ERRORS.includes(errors.toString())
      ) {
        this.contextInfo.nmvtisErrorList.pushOnce(errors.toString());
      }
    },
    onMStopYes: function() {
      this.contextInfo.mStopFlag = true;
      this.isMStopModal = false;
      this.removeError(5007);
    },
    onMStopNo: function() {
      this.isMStopModal = false;
      this.addError(5007);
    },

    genrateWarningMessages: function(warningMsgs) {
      if (warningMsgs.length) {
        warningMsgs.forEach(warning => {
          this.warningMessages.pushOnce(
            warning.errorCode + " - " + warning.errorMessage
          );
        });
      }
    },

    isMV06Reset: function() {
      document
        .getElementById("view-container-id")
        .querySelector("input")
        .focus();
      this.contextInfo.documentVerification = "";
    },

    onNextClick: function() {
      this.getFeeCalculationDate();
      this.validateCostOfRepairs();
      this.validateValueOfVehicle();
      this.validateValueAndCostOfRepairOfVeh();
      this.populateNmvtisValues();
      this.validateOdometerMiles();
      this.validateDocVerifiedOnNext();

      if (
        this.contextInfo.nmvtisVerificationFlag == true &&
        ((this.nmvtis.snapshots[0].conversationCode == "01" &&
          (this.contextInfo.documentVerification == "A" ||
            this.contextInfo.documentVerification == "Y")) ||
          (this.contextInfo.documentVerification == "Y" &&
            (this.nmvtis.snapshots[0].conversationCode == "11" ||
              this.nmvtis.snapshots[0].conversationCode == "06")))
      ) {
        this.setVehicleInfoFromNmvtis();
      }

      if (this.nmvtis.snapshots[0].conversationCode == "06") {
        this.$store.commit(
          MutationTypes.SET_UPDATEVEHICLE_NMVTIS_SNAPSHOT,
          this.nmvtis.snapshots[0]
        );
        this.snapshot = this.$store.state["nmvtis"].nmvtis.snapshots[0];
        if (
          this.feeDetails.feeRequest.feeAttributes.weightClassCodeInd == "Y"
        ) {
          this.setProrationCall();
        } else {
          this.calculateFee();
        }
      }
      if (this.nmvtis.snapshots[0].conversationCode == "01") {
        this.$store.commit(
          MutationTypes.SET_NEWTITLE_NMVTIS_SNAPSHOT,
          this.nmvtis.snapshots[0]
        );
        this.$router.push("/new_title/owner_lesse_info");
      }
      if (this.nmvtis.snapshots[0].conversationCode == "11") {
        this.$store.commit(
          MutationTypes.SET_SALVAGE_NMVTIS_SNAPSHOT,
          this.nmvtis.snapshots[0]
        );

        this.$router.push("/salvage_title/detail");
      }
    },

    onPreviousClick: function() {
      this.$router.push("/new_title/prompt");
    },

    nmvtisInquiryCall() {

    console.log("On Mounted log 4 nmvtisInquiryCall");

      let uuid = CONVERSATION_UTILS.uuid();
      let sot = "PA";
      let vinNum = "";
      let titleNum = "";

      if (this.inquiry.vinNum) {
        vinNum = this.inquiry.vinNum;
      } else if (
        this.checkforSuspendWid() &&
        this.contextInfo.originalInquiry &&
        this.contextInfo.originalInquiry.vinNum
      ) {
        vinNum = this.contextInfo.originalInquiry.vinNum;
      } else if (this.vehicle.vinNum) {
        vinNum = this.vehicle.vinNum;
      }

      if (this.inquiry.titleNum) {
        titleNum = this.inquiry.titleNum;
      } else if (
        this.checkforSuspendWid() &&
        this.contextInfo.originalInquiry &&
        this.contextInfo.originalInquiry.titleNum
      ) {
        titleNum = this.contextInfo.originalInquiry.titleNum;
      } else if (
        (this.nmvtis.snapshots[0].conversationCode == "11" ||
          this.nmvtis.snapshots[0].conversationCode == "06") &&
        this.inquiry.suspenseWID &&
        this.title.titleNum
      ) {
        titleNum = this.title.titleNum;
      }

      if (vinNum != "" || titleNum != "") {
        let nmvtisRequest = {
          vinNumber: vinNum,
          stateOfTitle: sot,
          titleNum: titleNum,
          corellatorId: uuid
        };
        var key = nmvtisRequest.vinNumber + nmvtisRequest.titleNum + sot + uuid;
        this.isLoading = true;
        let vm = this;
        httpUtil
          .post("/nmvtis/nmvtisusedvehicleinquiryrequest", nmvtisRequest)
          .then(response => {
            vm.nmvtisPolling(key);
          })
          .catch(errors => {
            vm.isLoading = false;
            if (this.$_.isArray(errors)) {
              errors.forEach(error => {
                vm.addError(error.errorCode);
              });
            }
           this.handleException(errors);
            vm.addError(2268);
            return;
          });
      } else {
        this.isLoading = false;
      }
    },
    async validateNmvtisVerificationFlag() {
    console.log("On Mounted log 3 validateNmvtisVerificationFlag");

      //BRU36964
      //BRU36965
      await httpUtil
        .get(
          "/referencedata?reftype=0621 - NMVTIS VERIFICATION ON - OFF&ref_cd=" +
            this.pageName
        )
        .then(response => {
          if (response.data) {
            var referenceDataArray = response.data;
            let obj = referenceDataArray.find(
              o => o.addlAttrName === "MV0621-PROJ-IN-USE"
            );
            if (obj.referenceCodeDesc == "Y") {
              this.contextInfo.nmvtisVerificationFlag = true;
              this.validateNmvtisCall();
            } else {
              this.addPageError(21056, this.errorTypes.info);
            }
            if (obj.referenceCodeDesc == "N") {
              this.addPageError(21057, this.errorTypes.info);
            }
          }
        })
        .catch(errors => {
          this.addError(errors);
        });
    },
    validateNmvtisForSuspenseWid: function() {
      //BRU36010
      if (
        this.checkforSuspendWid() &&
        this.contextInfo.originalInquiry &&
        this.contextInfo.originalInquiry.mcoProvided == CONSTANTS.STR_1 &&
        this.contextInfo.originalInquiry.nonPaTitleCd != "T" &&
        this.contextInfo.originalInquiry.vinNum &&
        (!this.title || !this.title.id || this.title.id == 0)
      ) {
        this.nmvtisForSuspenseWidInd = true;
      } else {
        this.nmvtisForSuspenseWidInd = false;
      }
    },
    validateNmvtisForTitleforce: function() {
      //BRU36475
      if (
        this.inquiry.titleNum &&
        this.inquiry.vinNum &&
        (!this.vehicle || !this.vehicle.id || this.vehicle.id == 0) &&
        (!this.title || !this.title.id || this.title.id == 0) &&
        this.contextInfo.titleForce == true
      ) {
        this.nmvtisForTitleForceInd = true;
      } else {
        this.nmvtisForTitleForceInd = false;
      }
    },
    validateNmvtisForTfAndWid: function() {
      //BRU36477
      //BRU36476
      if (
        this.checkforSuspendWid() &&
        this.contextInfo.originalInquiry &&
        this.contextInfo.originalInquiry.titleNum &&
        this.contextInfo.originalInquiry.vinNum &&
        (!this.vehicle || !this.vehicle.id || this.vehicle.id == 0) &&
        (!this.title || !this.title.id || this.title.id == 0) &&
        this.contextInfo.originalInquiry.titleForce == true
      ) {
        this.nmvtisForTfAndWidInd = true;
      } else {
        this.nmvtisForTfAndWidInd = false;
      }
    },
    validateNmvtisForWidAndMco: function() {
      //BRU36473
      if (
        this.checkforSuspendWid() &&
        this.inquiry.mcoProvided == CONSTANTS.STR_1 &&
        this.inquiry.vinNum &&
        (!this.inquiry.nonPaTitleCd || this.inquiry.nonPaTitleCd != "T") &&
        (!this.vehicle || !this.vehicle.id || this.vehicle.id == 0)
      ) {
        this.nmvtisForWidAndMcoInd = true;
      } else {
        this.nmvtisForWidAndMcoInd = false;
      }
    },
    validateNmvtisForVinAndMco: function() {
      //BRU36008
      if (
        this.inquiry.mcoProvided == CONSTANTS.STR_1 &&
        this.inquiry.vinNum &&
        (!this.inquiry.nonPaTitleCd || this.inquiry.nonPaTitleCd != "T") &&
        (!this.vehicle || !this.vehicle.vinNum)
      ) {
        this.nmvtisForVinAndMcoInd = true;
      } else {
        this.nmvtisForVinAndMcoInd = false;
      }
    },
   validateNmvtisVinAndOutOfState: function() {
      //BRU36464
      if (
        this.inquiry.vinNum &&
        this.inquiry.priorState &&
        (!this.inquiry.nonPaTitleCd || this.inquiry.nonPaTitleCd != "T") &&
        (!this.vehicle || !this.vehicle.id || this.vehicle.id == 0)
      ) {
        this.nmvtisForVinAndOutOfState = true;
      } else {
        this.nmvtisForVinAndOutOfState = false;
      }
    },
    validateNmvtisWidOutOfState: function() {
      //BRU36011
      //BRU36472
      if (
        this.checkforSuspendWid() &&
        this.contextInfo.originalInquiry &&
        (this.contextInfo.originalInquiry.vinNum &&
          this.contextInfo.originalInquiry.priorState) &&
        (!this.contextInfo.originalInquiry.nonPaTitleCd ||
          this.contextInfo.originalInquiry.nonPaTitleCd != "T") &&
        (!this.title || !this.title.id || this.title.id == 0)
      ) {
        this.nmvtisForWidOutOfState = true;
      } else {
        this.nmvtisForWidOutOfState = false;
      }
    },
    validateNmvtisWidForExistingPaRec: function() {
      //36470
      if (
        (this.checkforSuspendWid() &&
          this.contextInfo.originalInquiry &&
          (this.contextInfo.originalInquiry.titleNum &&
            this.contextInfo.originalInquiry.checkNum) &&
          (this.inquiry.nonPaTitleCd || this.inquiry.nonPaTitleCd != "T") &&
          (this.title && this.title.id && this.title.id != 0)) ||
        (this.contextInfo.originalInquiry &&
          this.contextInfo.originalInquiry.vinNum &&
          !this.contextInfo.originalInquiry.titleNum &&
          (this.inquiry.nonPaTitleCd || this.inquiry.nonPaTitleCd != "T") &&
          (this.title && this.title.id && this.title.id != 0))
      ) {
        this.nmvtisWidForExistingPaRec = true;
      } else {
        this.nmvtisWidForExistingPaRec = false;
      }
    },

    validateNmvtisWidAndExistingPaRrd: function() {
      //BRU32752
      if (
        (this.inquiry.titleNum &&
          this.inquiry.checkNum &&
          (!this.inquiry.nonPaTitleCd || this.inquiry.nonPaTitleCd != "T") &&
          this.title.titleNum) ||
        (this.inquiry.vinNum &&
          (!this.inquiry.titleNum ||
            this.inquiry.reassignFlag == CONSTANTS.STR_YES) &&
          (!this.inquiry.nonPaTitleCd || this.inquiry.nonPaTitleCd != "T") &&
          this.title.titleNum)
      ) {
        this.nmvtisForWidAndExistingPaRrd = true;
      } else {
        this.nmvtisForWidAndExistingPaRrd = false;
      }
    },
    validateNmvtisNonPATitle: function() {
      //BRU36009
      if (this.inquiry.nonPaTitleCd == "T" || this.title.nonPaTitleCd == "T") {
        this.noNmvtisCallInd = true;
        this.addPageError(21058, this.errorTypes.info);
      }
    },
    validateNmvtisNonPATitleAndWid: function() {
      //BRU36012
      if (
        this.checkforSuspendWid() &&
        this.contextInfo.originalInquiry &&
        (this.contextInfo.originalInquiry.nonPaTitleCd == "T" ||
          this.title.nonPaTitleCd == "T")
      ) {
        this.noNmvtisCallInd = true;
        this.addPageError(21058, this.errorTypes.info);
      }
    },
    validateNmvtisForPriorStateAndTitle: function() {
      //BRU39016
      if (this.inquiry.priorState && this.inquiry.priorStateTitle) {
        this.nmvtisForPriorStateAndTitle = true;
      } else {
        this.nmvtisForPriorStateAndTitle = false;
      }
    },
    validateTitleStopCodeMV01: function() {
      if (this.SOR === CONSTANTS.SOR_MVDLS) {
        if (
          this.title &&
          this.title.titleStopCds &&
          this.title.titleStopCds.length > 0
        ) {
          this.title.titleStopCds.forEach(stopCds => {
            if (this.nmvtis.snapshots[0].conversationCode == "01") {
              //BRU42570
              if (
                stopCds.stopCd == "M" &&
                this.vinHist &&
                this.vinHist[0].cancelReasonCode == "5"
              ) {
                this.isMStopModal = true;
              } else if (
                stopCds.stopCd &&
               !CONSTANTS.STOP_CODE_MATRIX_MV01.includes(stopCds.stopCd)
              ) {
                this.addError(this.vehicleStopcodes[stopCds.stopCd]);
              } else if (stopCds.stopCd && stopCds.stopCd == "R") {
                this.addPageError("0000", this.errorTypes.warning);
              } else {
                this.removeError(this.vehicleStopcodes[stopCds.stopCd]);
              }
            }
          });
        }
      } else {
        this.validateTitleStopCdMV01Carats();
      }
    },

    validateTitleStopCodeMv11AndMv06: function() {
      if (
        this.title &&
        this.title.titleStopCds &&
        this.title.titleStopCds.length > 0
      ) {
        this.title.titleStopCds.forEach(stopCds => {
          if (
            stopCds.stopCd &&
            ((!CONSTANTS.STOP_CODE_MATRIX_MV11.includes(stopCds.stopCd) &&
              this.nmvtis.snapshots[0].conversationCode == "11") ||
              (!CONSTANTS.STOP_CODE_MATRIX_MV06.includes(stopCds.stopCd) &&
                this.nmvtis.snapshots[0].conversationCode == "06"))
          ) {
            this.addError(this.vehicleStopcodes[stopCds.stopCd]);
          } else if (stopCds.stopCd && stopCds.stopCd == "R") {
            this.addPageError("0000", this.errorTypes.warning);
          } else {
            this.removeError(this.vehicleStopcodes[stopCds.stopCd]);
          }
        });
      }
    },

    async validateNmvtisCall() {

    console.log("On Mounted log 4 validateNmvtisCall");

      if (this.SOR === CONSTANTS.SOR_CARATS) {
        this.validateNmvtisForSuspenseWid();
        this.validateNmvtisForTitleforce();
        this.validateNmvtisForWidAndMco();
        this.validateNmvtisForVinAndMco();
      }
      this.validateNmvtisVinAndOutOfState();
      this.validateNmvtisWidAndExistingPaRrd();
      this.validateNmvtisForPriorStateAndTitle();
      this.validateNmvtisWidOutOfState();
      this.validateNmvtisWidForExistingPaRec();
      this.validateNmvtisForTfAndWid();
      if (
        this.noNmvtisCallInd == false &&
        (this.nmvtisForSuspenseWidInd == true ||
          this.nmvtisForTitleForceInd == true ||
          this.nmvtisForTfAndWidInd == true ||
          this.nmvtisForWidAndMcoInd == true ||
          this.nmvtisForVinAndMcoInd == true ||
          this.nmvtisForVinAndOutOfState == true ||
          this.nmvtisForWidAndExistingPaRrd == true ||
          this.nmvtisForWidOutOfState == true ||
          this.nmvtisWidForExistingPaRec == true ||
          this.nmvtis.snapshots[0].conversationCode == "11" ||
          this.nmvtis.snapshots[0].conversationCode == "06")
      ) {
        await this.nmvtisInquiryCall();
      }
    },
    ncicServiceCall: function() {
      let uuid = CONVERSATION_UTILS.uuid();
      let vinNum = "";

      if (this.inquiry && this.inquiry.vinNum) {
        vinNum = this.inquiry.vinNum;
      } else if (this.vehicle && this.vehicle.vinNum) {
        vinNum = this.vehicle.vinNum;
      }

      if (vinNum) {
        let ncicRequest = {
          corellatorId: uuid,
          messageKey: "QV",
          vinNumber: vinNum
        };
        let vm = this;
        this.isNcicLoading = true;
        httpUtil
          .post("/interfaces/ncicstolenvehicleuirequest", ncicRequest)
          .then(response => {
            if (response.data && response.data.message && 
            response.data.message.includes(CONSTANTS.NCIC_V1124)) {
              vm.isNcicLoading = false;
              vm.addError(21130);
            } else if(response.data && response.data.msgSequenceNumber) {
              vm.ncicPolling(response.data.msgSequenceNumber);
            }else{
               vm.isNcicLoading = false;
               vm.addError(21119);
            }
          })
          .catch(errors => {
            vm.isNcicLoading = false;
            vm.addError(21119);
            if (this.$_.isArray(errors)) {
              errors.forEach(error => {
                vm.addError(error.errorCode);
              });
            }
          this.handleException(errors);
          });
      }
    },
    validateFleetVehicle: function() {
      //BRU26036
      if (this.title.fleetInd == "1") {
        this.addError(1077);
      } else {
        this.removeError(1077);
      }
    },
    validateNonPaTitle: function() {
      //BRU26037
      if (this.title && this.title.nonPaTitleCd == "F") {
        this.addError(2158);
      } else {
        this.removeError(2158);
      }
    },
    validateArpInd: function() {
      //BRU26038
      if (this.title.arpInd == "1") {
        this.addError(1078);
      } else {
        this.removeError(1078);
      }
    },
    validateFromPlateNum: function() {
      //BRU26048
      if (
       this.inquiry.titleNum &&
        this.transferInquiry.fromTitleNum &&
        this.transferInquiry.fromPlateNum &&
        this.title.titleNum &&
        this.plate.plateNum &&
        this.vehicle.vinNum &&
        this.transferInquiry.fromPlateNum == this.plate.plateNum
      ) {
        this.addError(1513);
      } else {
        this.removeError(1513);
      }
    },
    validateFromPlateNumDeadPlateReason: function() {
      //BRU26052
      if (
        this.transferInquiry.fromTitleNum &&
        this.transferInquiry.fromPlateNum &&
        !this.fromPlate &&
        !this.fromPlate.plateNum
      ) {
        var isPlateOnly = false;
        var isdeadPlate = false;
        if (this.fromPlate.plateInfos) {
          this.fromPlate.plateInfos.forEach((plateInfo, index) => {
            if (
              plateInfo.deadPlateReasCd == "A" ||
              plateInfo.deadPlateReasCd == "9"
            ) {
              isdeadPlate = true;
            }
            if (plateInfo.plateOnlyInd == "Y") {
              isPlateOnly = true;
            }
          });
        }
        if (isPlateOnly == true || isdeadPlate == true) {
          this.addError(1000);
        } else {
          this.removeError(1000);
        }
      }
    },
    validateFromPlateNumAndOwner: function() {
      //BRU26056
      if (
        this.transferInquiry.fromTitleNum &&
        this.transferInquiry.fromPlateNum &&
        this.fromPlate &&
        this.fromPlate.plateNum &&
        this.fromTitle &&
        this.fromTitle.id &&
        this.fromTitle.id != 0 &&
        (!this.fromTitle.owner.id || this.fromTitle.owner.id == 0)
      ) {
        this.addPageError(21061, this.errorTypes.warning);
      }
    },
    validateFromTitleAsBiennial: function() {
      //BRU26060
      if (
        this.transferInquiry.fromTitleNum &&
        this.transferInquiry.fromPlateNum &&
        this.fromRegistration.id &&
        this.fromRegistration &&
        this.fromRegistration.biennialExmpFlag
      ) {
        this.addError(1015);
      } else {
        this.removeError(1015);
      }
    },
    validateVinInRefTable660: function() {
      //BRU 36133
      if (this.inquiry.vinNum) {
        httpUtil
          .get(
            "/referencedata?reftype=0660 - STOLEN VEHICLES BY VIN TBL&ref_cd=" +
              this.inquiry.vinNum
          )
          .then(response => {
            if (response.data.length > 0) {
              this.addError(1136);
           } else {
              this.removeError(1136);
            }
          })
          .catch(errors => {});
      }
    },
    validateTransferTitleForSalvageInd: function() {
      //BRU37662
      if (
        ((this.inquiry.titleNum && this.inquiry.checkNum) ||
          (this.inquiry.vinNum &&
            this.inquiry.vinNum == this.vehicle.vinNum)) &&
        this.title.vehJunkInd == "1"
      ) {
        this.addError(1134);
      } else {
        this.removeError(1134);
      }
    },
    validateTransferTitleForNonPaTitleCd: function() {
      //BRU37663
      if (
        ((this.inquiry.titleNum && this.inquiry.checkNum) ||
          (this.inquiry.vinNum &&
            this.inquiry.vinNum == this.vehicle.vinNum)) &&
        this.title &&
        this.title.titleNum &&
        (this.title.nonPaTitleCd == "D" || this.title.nonPaTitleCd == "T")
      ) {
        this.addError(1473);
      } else {
        this.removeError(1473);
      }
    },
    validateDocVerified: function() {
      //BRU36126, BRU36127, BRU36128, BRU36129
      if (
        this.contextInfo.documentVerification == "Y" ||
        this.contextInfo.documentVerification == "N"
      ) {
        this.contextInfo.vehCostOfRepairs = null;
        this.contextInfo.vehValue = null;
        this.disableOnDocVerifiedInd = true;
      } else {
        this.disableOnDocVerifiedInd = false;
      }
      if (this.contextInfo.documentVerification == "A") {
        this.valueRequired = "required";
        //BRU36128
        if (
          this.contextInfo.vehCostOfRepairs == null ||
          this.contextInfo.vehCostOfRepairs === "" ||
          this.contextInfo.vehValue == null ||
          this.contextInfo.vehValue === ""
        ) {
          this.addError(21099);
        }
      } else {
        this.valueRequired = "";
        this.removeError(21099);
      }
      //BRU37532
      if (
        this.contextInfo.nmvtisErrorList &&
        this.contextInfo.nmvtisErrorList.length > 0
      ) {
        this.contextInfo.nmvtisErrorList.forEach(error => {
          if (this.contextInfo.documentVerification == "O") {
            this.removeError(error);
          } else {
            this.addError(error);
          }
        });
      }
    },
    validateValueOfVehicle: function() {
      //BRU36130
      if (
        this.contextInfo.vehValue != null &&
        parseInt(this.contextInfo.vehValue) <= 0
      ) {
        this.addError(2288);
      } else {
        this.removeError(2288);
      }
    },

    //BRU37532
    filterDocsVerifiedBasedOnFGAC(resrc) {
      let array = [];
      this.nmvtis.snapshots[0].conversationCode == "06" ? ["A"] : [];
      if (
        FGAC_RULES.checkResrcIsPresent(
          resrc,
          this.$store.state.userContext.authority
        )
      )
        return array;
      array.push("O");
      return array;
    },

    validateStolenVehicle: function() {
      //BRU37067
      if (
        this.nmvtisResponse.response.theftReport &&
        this.nmvtisResponse.response.theftReport[0].theftReportCaseID
      ) {
        this.addError(2284);
      } else {
        this.removeError(2284);
      }
    },
    validateVehicleMultiplePointers: function() {
      //BRU36976
      if (
        this.nmvtisResponse.response.vehicleNoTitleHistory &&
        this.nmvtisResponse.response.vehicleNoTitleHistory.length > 1
      ) {
        this.addError(4110);
      } else {
        this.removeError(4110);
      }
    },
    validatePointersAndSOT: function() {
      //BRU36975
      if (
        this.nmvtisResponse &&
        this.nmvtisResponse.response.jurisdictionVehicle &&
        this.nmvtisResponse.response.jurisdictionVehicle.length == 1 &&
        this.inquiry &&
        this.inquiry.priorState &&
        this.nmvtisResponse.response.jurisdictionVehicle[0]
          .titleIssuingAuthorityName != this.inquiry.priorState
      ) {
        this.addError(20071);
      } else {
        this.removeError(20071);
      }
    },
    validateVinNum: function() {
      //BRU36974
      if (
        !this.nmvtisResponse.response.vehicleNoTitleHistory ||
        (this.nmvtisResponse.response.vehicleNoTitleHistory.length == 1 &&
          (!this.nmvtisResponse.response.vehicleNoTitleHistory[0]
            .titleIssuingAuthorityName || 
            !this.nmvtisResponse.response.vehicleNoTitleHistory[0]
            .titleIdentificationID) )
      ) {
        this.addPageError(20072, this.errorTypes.warning);
      } else {
        this.removeError(20072);   
      }
    },
    async validatePostNmvtisCall() {
      console.log("On Mounted log 6 validatePostNmvtisCall");
      this.addPageError(21059, this.errorTypes.info);
      this.validateVinNum();
      await this.validatePointersAndSOT();
      await this.validateVehicleMultiplePointers();
      await this.validateStolenVehicle();
      await this.validateBrandMapping();
      await this.validateDocVerified();
      await this.validateBrandMessage();
    },
    validateCostOfRepairs: function() {
      //BRU36131
      if (
        this.contextInfo.vehCostOfRepairs != null &&
        parseInt(this.contextInfo.vehCostOfRepairs) <= 0
      ) {
        this.addError(2289);
      } else {
        this.removeError(2289);
      }
    },
    validateValueAndCostOfRepairOfVeh: function() {
      //BRU36132
      if (
        this.contextInfo.vehCostOfRepairs &&
        this.contextInfo.vehValue &&
        parseInt(this.contextInfo.vehValue) <
          parseInt(this.contextInfo.vehCostOfRepairs)
      ) {
        this.addError(2290);
      } else {
        this.removeError(2290);
      }
    },

    validateAbandVeh: function() {
      //BRU38339
      if (
        this.inquiry.titleNum &&
        this.inquiry.checkNum &&
        this.title.abandVehInd == "Y"
      ) {
        this.addError(5062);
      } else {
        this.removeError(5062);
      }
    },

    getPaBrandName: function() {
      let vm = this;
      httpUtil
        .get("/referencedata/transform/0623 - NMVTIS BRAND MAPPING")
        .then(response => {
          if (response.data) {
            vm.nmvtisPaBrandMapping = response.data;
          }
        })
        .catch(errors => {
          this.addError(errors);
        });
    },
    async validateNcicCall() {
      if (this.inquiry.nonPaTitleCd != "T") {
        await this.ncicServiceCall();
      }
    },
    validateTitleStopCdMV01Carats: function() {
      //BRU26064
      if (this.inquiry.titleNum) {
        if (
          this.title &&
          this.title.titleStopCds &&
          this.title.titleStopCds.length > 0
        ) {
          this.title.titleStopCds.forEach(titleStopcd => {
            if (
              titleStopcd.stopCd &&
              titleStopcd.stopCd in this.vehicleStopcodes
            ) {
              this.addError(this.vehicleStopcodes[titleStopcd.stopCd]);
            } else {
              this.removeError(this.vehicleStopcodes[titleStopcd.stopCd]);
            }
          });
        }
      }
    },
    populateNmvtisValues: function() {
      this.contextInfo.vinPointerOdometer = this.vinPointerOdometer;
      this.contextInfo.statePointerOdometer = this.stateOdometerReading;
      this.contextInfo.nmvtisBrandList = this.fullBrandList;
      this.contextInfo.operatorId = this.$store.state.userContext.oprId;
    },
    validateBrandMapping: function() {
      //BRU37070, BRU37065, BRU37071, BRU37072, BRU37073, BRU37074, BRU37075
      let brandCode = "";
      let item;
      if (this.fullBrandList && this.fullBrandList.length > 0) {
        for (var j = 0; j < this.fullBrandList.length; j++) {
          this.brandCodes.pushOnce(this.fullBrandList[j].brand);
        }

        for (var i = 0; i < this.fullBrandList.length; i++) {
          item = this.fullBrandList[i];

          brandCode = item.brand;

          //Flood damage and Salt Water Damage
          if (
            brandCode == CONSTANTS.NMVTIS_BRAND_01 ||
            brandCode == CONSTANTS.NMVTIS_BRAND_04
          ) {
            if (this.vehicle) {
              if (!this.vehicle.reconstVehCd) {
                this.contextInfo.reconstVehCd = "W";
              }
              if (this.vehicle.reconstVehCd == "A") {
                this.contextInfo.reconstVehCd = "J";
              }
              if (this.vehicle.reconstVehCd == "C") {
                this.contextInfo.reconstVehCd = "K";
              }
              if (this.vehicle.reconstVehCd == "D") {
                this.contextInfo.reconstVehCd = "O";
              }
              if (this.vehicle.reconstVehCd == "H") {
                this.contextInfo.reconstVehCd = "U";
              }
              if (this.vehicle.reconstVehCd == "I") {
                this.contextInfo.reconstVehCd = "V";
              }
              if (this.vehicle.reconstVehCd == "L") {
                this.contextInfo.reconstVehCd = "Y";
              }
              if (this.vehicle.reconstVehCd == "M") {
                this.contextInfo.reconstVehCd = "G";
              }
              if (this.vehicle.reconstVehCd == "N") {
                this.contextInfo.reconstVehCd = "F";
              }
              if (this.vehicle.reconstVehCd == "P") {
                this.contextInfo.reconstVehCd = "X";
              }
              if (this.vehicle.reconstVehCd == "R") {
                this.contextInfo.reconstVehCd = "B";
              }
              if (this.vehicle.reconstVehCd == "T") {
                this.contextInfo.reconstVehCd = "E";
              }
            }
          }
          //Kit and Replica
          else if (
            brandCode == CONSTANTS.NMVTIS_BRAND_06 ||
            brandCode == CONSTANTS.NMVTIS_BRAND_30
          ) {
            this.contextInfo.vehMakeModelCd = CONSTANTS.SPCO;
          }
          //Dismantled
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_07) {
            this.addError(2246);
            this.addError(7973);
          }
          //Junk
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_08) {
            this.addError(2247);
          }
          //ReConstructed
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_10 && this.vehicle) {
            if (!this.vehicle.reconstVehCd) {
              this.contextInfo.reconstVehCd = "M";
            }
            if (this.vehicle.reconstVehCd == "A") {
              this.contextInfo.reconstVehCd = "P";
            }
            if (this.vehicle.reconstVehCd == "B") {
              this.contextInfo.reconstVehCd = "O";
            }
            if (this.vehicle.reconstVehCd == "C") {
              this.contextInfo.reconstVehCd = "L";
            }
            if (this.vehicle.reconstVehCd == "E") {
              this.contextInfo.reconstVehCd = "U";
            }
            if (this.vehicle.reconstVehCd == "F") {
              this.contextInfo.reconstVehCd = "V";
            }
            if (this.vehicle.reconstVehCd == "J") {
              this.contextInfo.reconstVehCd = "X";
            }
            if (this.vehicle.reconstVehCd == "K") {
              this.contextInfo.reconstVehCd = "Y";
            }
            if (this.vehicle.reconstVehCd == "N") {
              this.contextInfo.reconstVehCd = "I";
            }
           if (this.vehicle.reconstVehCd == "R") {
              this.contextInfo.reconstVehCd = "D";
            }
            if (this.vehicle.reconstVehCd == "T") {
              this.contextInfo.reconstVehCd = "H";
            }
            if (this.vehicle.reconstVehCd == "W") {
              this.contextInfo.reconstVehCd = "G";
            }
          }
          //Salvage—Damage or Not Specified
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_11) {
            this.contextInfo.vehJunkInd = CONSTANTS.JUNK_IND_1;
          }
          //Salvage Retention
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_16) {
            this.addError(2276);
          }
          //Prior Taxi or Original Taxi
          else if (
            brandCode == CONSTANTS.NMVTIS_BRAND_17 ||
            brandCode == CONSTANTS.NMVTIS_BRAND_19
          ) {
            this.contextInfo.formerTaxiInd = "1";
          }
          //Prior Police and Original Police
          else if (
            brandCode == CONSTANTS.NMVTIS_BRAND_18 ||
            brandCode == CONSTANTS.NMVTIS_BRAND_20
          ) {
            this.contextInfo.formerPoliceInd = "1";
          }
          //Agricultural Vehicle
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_26) {
            this.contextInfo.vehUseCd = "H";
          }
          //Logging Vehicle
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_27) {
            this.contextInfo.vehUseCd = "L";
          }
          //Street Rod
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_28) {
            this.addError(2285);
          }
          //Totaled
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_31) {
            this.addError(2248);
          }
          //Owner Retained
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_32) {
            this.addError(2249);
          }
          //Parts Only
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_35) {
            this.addError(2250);
            this.addError(7973);
          }
          //Recovered Theft
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_36 && this.vehicle) {
            if (!this.vehicle.reconstVehCd) {
              this.contextInfo.reconstVehCd = "T";
            }
            if (this.vehicle.reconstVehCd == "B") {
              this.contextInfo.reconstVehCd = "J";
            }
            if (this.vehicle.reconstVehCd == "D") {
              this.contextInfo.reconstVehCd = "P";
            }
            if (this.vehicle.reconstVehCd == "G") {
              this.contextInfo.reconstVehCd = "U";
            }
            if (this.vehicle.reconstVehCd == "M") {
              this.contextInfo.reconstVehCd = "H";
            }
            if (this.vehicle.reconstVehCd == "O") {
              this.contextInfo.reconstVehCd = "X";
            }
            if (this.vehicle.reconstVehCd == "R") {
              this.contextInfo.reconstVehCd = "A";
            }
            if (this.vehicle.reconstVehCd == "W") {
              this.contextInfo.reconstVehCd = "E";
            }
            if (
              this.brandCodes &&
              this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_49)
            ) {
              this.removeErrorFromList(2253);
            }
          }
          //Vehicle Safety Defect Uncorrected
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_41) {
            this.addPageError(2251, this.errorTypes.warning);
          }
          //VIN Replaced by a New State Assigned VIN
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_43) {
            this.addError(2252);
          }
          //Grey Market
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_45) {
            if (this.nmvtis.snapshots[0].conversationCode == "06") {
              this.addError(6450);
            } else {
              this.removeError(6450);
            }
            if (
              !this.title.greyMarketInd ||
              this.title.greyMarketInd == CONSTANTS.STR_0
            ) {
              this.contextInfo.greyMarketInd = CONSTANTS.STR_1;
            }
          }
          //Grey Market
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_46) {
            if (
              !this.title.greyMarketInd ||
              this.title.greyMarketInd == CONSTANTS.STR_1
            ) {
              this.contextInfo.greyMarketInd = CONSTANTS.STR_0;
            }
          }
          //Manufacturer Buy Back
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_47) {
            if (
              !this.title.lemonVehIndicator ||
              this.title.lemonVehIndicator == CONSTANTS.LEMON_VEHICLE_IND_0
            ) {
              this.contextInfo.lemonVehIndicator =
                CONSTANTS.LEMON_VEHICLE_IND_1;
            }
          }
          //Salvage—Stolen
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_49) {
            this.addError(2253);
          }
          //Salvage—Reasons Other Than Damage or Stolen
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_50) {
            if (this.title && !this.title.abandVehInd) {
              this.contextInfo.abandVehInd = CONSTANTS.VEH_ABAN_IND_I;
            }
            this.addPageError(2254, this.errorTypes.warning);
          }
          //Prior Non-Repairable/ Repaired
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_52) {
            this.addError(2255);
          }
          //Crushed
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_53) {
            this.addError(2256);
          }
          //Actual
          else if (
            brandCode == CONSTANTS.NMVTIS_BRAND_68 &&
            this.title &&
            !this.title.odomQualifyCd
          ) {
            this.contextInfo.nmvtisOdomQualifyCd =
              CONSTANTS.ODO_QUALIF_CD_0_ACTUAL_MILE;
          }
          //Not Actual
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_69) {
            this.contextInfo.nmvtisOdomQualifyCd = CONSTANTS.ODO_QUALIF_CD_2;
          }
          //Not Actual—Odometer Tampering Verified
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_70) {
            this.contextInfo.nmvtisOdomQualifyCd =
              CONSTANTS.ODO_QUALIF_CD_3_NOT_ACTUAL_MILES;
          }
          //Exempt from Odometer Disclosure
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_71) {
            this.contextInfo.nmvtisOdomQualifyCd =
              CONSTANTS.ODO_QUALIF_CD_4_EXMPT_FED_LAW;
            this.addPageError(2257, this.errorTypes.warning);
          }
          //Exceeds Mechanical Limits
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_72) {
            if (
              this.brandCodes.indexOf(CONSTANTS.NMVTIS_BRAND_78) == -1 ||
              parseInt(this.brandCodes.indexOf(CONSTANTS.NMVTIS_BRAND_72)) >
                parseInt(this.brandCodes.indexOf(CONSTANTS.NMVTIS_BRAND_78))
            ) {
              this.contextInfo.nmvtisOdomQualifyCd = CONSTANTS.ODO_QUALIF_CD_1;
            }
          }
          //Odometer May Be Altered
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_73) {
            this.addError(2258);
          }
          //Odometer Replaced
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_74) {
            this.addError(2259);
          }
          //Odometer Discrepancy
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_76) {
            this.addError(1514);
          }
          //Call Title Division
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_77) {
            this.addError(2260);
          }
          //Pending Junk Automobile
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_90) {
            this.addError(2261);
          }
          //Pending Junk Automobile
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_91) {
            this.addError(2262);
          }
          //Rebuilt
          else if (brandCode == CONSTANTS.NMVTIS_BRAND_09 && this.vehicle) {
            if (!this.vehicle.reconstVehCd) {
              this.contextInfo.reconstVehCd = "R";
            }
            if (this.vehicle.reconstVehCd == "E") {
              this.contextInfo.reconstVehCd = "J";
            }
            if (this.vehicle.reconstVehCd == "F") {
              this.contextInfo.reconstVehCd = "K";
            }
            if (this.vehicle.reconstVehCd == "G") {
              this.contextInfo.reconstVehCd = "O";
            }
            if (this.vehicle.reconstVehCd == "H") {
              this.contextInfo.reconstVehCd = "P";
            }
            if (this.vehicle.reconstVehCd == "I") {
              this.contextInfo.reconstVehCd = "L";
            }
            if (this.vehicle.reconstVehCd == "M") {
              this.contextInfo.reconstVehCd = "D";
            }
            if (this.vehicle.reconstVehCd == "N") {
              this.contextInfo.reconstVehCd = "C";
            }
            if (this.vehicle.reconstVehCd == "T") {
              this.contextInfo.reconstVehCd = "A";
            }
            if (this.vehicle.reconstVehCd == "U") {
              this.contextInfo.reconstVehCd = "X";
            }
            if (this.vehicle.reconstVehCd == "V") {
              this.contextInfo.reconstVehCd = "Y";
            }
            if (this.vehicle.reconstVehCd == "W") {
              this.contextInfo.reconstVehCd = "B";
            }
            if (this.brandCodes) {
              if (this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_07)) {
                this.removeErrorFromList(2246);
              }
              if (
                this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_07) ||
                this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_35)
              ) {
                this.removeErrorFromList(7973);
              }
              if (this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_08)) {
                this.removeErrorFromList(2247);
              }
              if (this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_31)) {
                this.removeErrorFromList(2248);
              }
              if (this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_35)) {
                this.removeErrorFromList(2250);
              }
              if (this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_50)) {
                this.removePageError(2254);
              }
              if (this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_11)) {
                this.contextInfo.vehJunkInd = CONSTANTS.JUNK_IND_0;
              }
            }
          } else if (brandCode == CONSTANTS.NMVTIS_BRAND_38) {
            if (
              this.brandCodes &&
              this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_32)
            ) {
              this.removeErrorFromList(2249);
            }
          } else if (brandCode == CONSTANTS.NMVTIS_BRAND_42) {
            if (
              this.brandCodes &&
              this.brandCodes.includes(CONSTANTS.NMVTIS_BRAND_41)
            ) {
              this.removePageError(2251);
            }
          }

          if (
            (this.nmvtis.snapshots[0].conversationCode == "06" ||
              this.nmvtis.snapshots[0].conversationCode == "11") &&
            (brandCode == CONSTANTS.NMVTIS_BRAND_01 ||
              brandCode == CONSTANTS.NMVTIS_BRAND_04 ||
              brandCode == CONSTANTS.NMVTIS_BRAND_06 ||
              brandCode == CONSTANTS.NMVTIS_BRAND_09 ||
              brandCode == CONSTANTS.NMVTIS_BRAND_10 ||
              brandCode == CONSTANTS.NMVTIS_BRAND_30 ||
              brandCode == CONSTANTS.NMVTIS_BRAND_36)
          ) {
            this.addError(21113);
          } else {
            this.removeErrorFromList(21113);
          }
          this.setAdditionalBrandMessage(brandCode);
        }
      }
    },
    removeErrorOnKey: function() {
      if (this.contextInfo.documentVerification == "A") {
        if (
          this.contextInfo.vehCostOfRepairs != null &&
          this.contextInfo.vehCostOfRepairs !== "" &&
          this.contextInfo.vehValue != null &&
          this.contextInfo.vehValue !== ""
        ) {
          this.removeError(21099);
        } else {
          this.addError(21099);
        }
      }
    },
    formatValue: function(val) {
      if (val) {
        return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      }
    },
    setVehicleInfoFromNmvtis: function() {
      if (this.contextInfo.nmvtisOdomQualifyCd) {
        this.title.odomQualifyCd = this.contextInfo.nmvtisOdomQualifyCd;
      }
      if (this.contextInfo.vehMakeModelCd) {
        this.vehicle.vehMakeModelCd = this.contextInfo.vehMakeModelCd;
      }
      if (this.contextInfo.reconstVehCd) {
        this.vehicle.reconstVehCd = this.contextInfo.reconstVehCd;
        this.contextInfo.currReconVal = this.contextInfo.reconstVehCd;
      }
      if (this.contextInfo.vehJunkInd) {
        this.title.vehJunkInd = this.contextInfo.vehJunkInd;
      }
      if (this.contextInfo.vehUseCd) {
        this.vehicle.vehUseCd = this.contextInfo.vehUseCd;
      }
      if (this.contextInfo.greyMarketInd) {
        this.title.greyMarketInd = this.contextInfo.greyMarketInd;
      }
      if (this.contextInfo.abandVehInd) {
        this.title.abandVehInd = this.contextInfo.abandVehInd;
      }
      if (this.contextInfo.formerTaxiInd) {
        this.vehicle.formerTaxiInd = this.contextInfo.formerTaxiInd;
      }
      if (this.contextInfo.formerPoliceInd) {
        this.vehicle.formerPoliceInd = this.contextInfo.formerPoliceInd;
      }
      if (this.contextInfo.lemonVehIndicator) {
        this.title.lemonVehIndicator = this.contextInfo.lemonVehIndicator;
      }
    },
    setAdditionalBrandMessage: function(brandCode) {
      if (
        brandCode == CONSTANTS.NMVTIS_BRAND_01 ||
        brandCode == CONSTANTS.NMVTIS_BRAND_04 ||
        brandCode == CONSTANTS.NMVTIS_BRAND_09 ||
        brandCode == CONSTANTS.NMVTIS_BRAND_10 ||
        brandCode == CONSTANTS.NMVTIS_BRAND_36
      ) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_FLOOD
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_07) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_DISMANTLED
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_08) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_JUNK
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_16) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_SALVAGE_RETENTION
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_28) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_STREET_ROD
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_31) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_TOTALED
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_32) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_OWNER_RETAINED
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_35) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_PARTS_ONLY
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_43) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_VIN_REPLACED
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_49) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_SALVAGE_STOLEN
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_52) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_MESSAGE_52
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_53) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_CRUSHED
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_73) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_ODOM_ALTER
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_74) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_ODOM_REPLACE
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_76) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_ODOM_DISCREPANCY
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_77) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_TITLE_DIVISION
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_90) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_PENDING_JUNK
        );
      } else if (brandCode == CONSTANTS.NMVTIS_BRAND_91) {
        this.contextInfo.additionalBrandMessages.pushOnce(
          CONSTANTS.NMVTIS_BRAND_JUNK_AUTO
        );
      }
    },
    validateValueAndCostOfRepairs: function() {
      //BRU38182
      this.disableOnDocVerifiedIndForMv06 = true;
    },
    filterForMv06: function(arr) {
      //BRU38430
      if (this.nmvtis.snapshots[0].conversationCode == "06") {
        return arr.filter(item => {
          return item.referenceCode != "A";
        });
      } else {
        return arr;
      }
    },
    //BRU38640
    validateOdometerMiles: function() {
      if (
        this.nmvtis.snapshots[0].conversationCode == "06" &&
        this.contextInfo.documentVerification == "Y" &&
        this.contextInfo.nmvtisVerificationFlag == true &&
        this.vinPointerOdometer > 0 &&
        this.vinPointerOdometer > parseInt(this.title.odomReadQty) &&
        (this.title.odomQualifyCd == "" ||
          this.title.odomQualifyCd == null ||
          this.title.odomQualifyCd == "0")
      ) {
        this.addError(2282);
      } else {
        this.removeError(2282);
      }
    },
    validateMv01OnLoadBRUs: function() {
      console.log("On Mounted log 2 validateMv01OnLoadBRUs");

      if (
        this.contextInfo.fromTitlePrompt == true &&
        this.nmvtis.snapshots[0].conversationCode == "01"
      ) {
        this.validateNmvtisVerificationFlag();
        this.validateNcicCall();
        this.validateTitleStopCodeMV01();
        this.validateVinInRefTable660();
        this.validateTransferTitleForSalvageInd();
        this.validateTransferTitleForNonPaTitleCd();
        this.validateFromPlateNum();
        this.validateFromPlateNumDeadPlateReason();
        this.validateFromPlateNumAndOwner();
        this.validateFromTitleAsBiennial();
        this.validateNmvtisNonPATitle();
        this.validateNmvtisNonPATitleAndWid();
        this.contextInfo.fromTitlePrompt = false;
      }
    },
    validateMv06OnLoadBRUs: function() {
      if (
        this.contextInfo.fromUpdateVehicle == true &&
        this.nmvtis.snapshots[0].conversationCode == "06"
      ) {
        this.validateTitleStopCodeMv11AndMv06();
        this.validateValueAndCostOfRepairs();
        this.validateNmvtisVerificationFlag();
        this.contextInfo.fromUpdateVehicle = false;
      }
    },
    validateMv11OnLoadBRUs: function() {
      if (
        this.contextInfo.fromSalvagePrompt == true &&
        this.nmvtis.snapshots[0].conversationCode == "11"
      ) {
        this.validateTitleStopCodeMv11AndMv06();
        this.validateNmvtisVerificationFlag();
        this.validateAbandVeh();
        this.contextInfo.fromSalvagePrompt = false;
      }
    },
    validateCommonOnLoadBRUs: function() {
      this.getPaBrandName();
      this.validateDocVerified();
      this.validateFleetVehicle();
      this.validateNonPaTitle();
      this.validateArpInd();
    },
    removeErrorFromList: function(error) {
      if (
        this.contextInfo.nmvtisErrorList &&
        this.contextInfo.nmvtisErrorList.length > 0 &&
        error &&
        this.contextInfo.nmvtisErrorList.includes(error.toString())
      ) {
        this.contextInfo.nmvtisErrorList.splice(
          this.contextInfo.nmvtisErrorList.indexOf(error.toString()),
          1
        );
      }
      this.removeError(error);
    },
    //BRU50376, BRU50377
    validateDocVerifiedOnNext: function() {
      if (!this.contextInfo.documentVerification) {
        this.addError(21114);
      } else {
        this.removeError(21114);
      }

      if (this.contextInfo.documentVerification == "N") {
        this.addError(4107);
      } else {
        this.removeError(4107);
      }
    },
    setProrationCall: function() {
      let tagType = ["08", "10", "21", "22", "23", "25", "33", "36", "I9"];
      if (
        !tagType.includes(this.plate.plateTypeCd) &&
        this.$store.state.SOR == "MVDLS" &&
        !(
          this.fromRegistration.numYearRegister == 99 ||
          this.registration.numYearRegister == 99
        )
      ) {
        //Calculate Prorate
        this.proRateFromNonRenew();
        this.feeCalculationProRate();
      } else {
       this.feeDetails.feeRequest.feeAttributes.prorateFee = null;
        this.calculateFee();
      }
    },
    setNmvtisTimeout(receivedNmvtisData) {
      if (receivedNmvtisData == false) {
        //BRU37066
        this.addError(2275);
      } else {
        this.removeError(2275);
      }
    },
    nmvtisPolling(key) {
    console.log("On Mounted log 5 nmvtisPolling");
      var counter = 0;
      let vm = this;
      var timer = setInterval(function() {
        counter++;
        httpUtil
          .get("/nmvtis/pollingnmvtisstatus/" + key)
          .then(response => {
            vm.nmvtisResponse = response.data;
           // console.log("JSON-->"+json.stringify(response.data))
            if (response.data) {
              vm.receivedNmvtisData = true;
              vm.isLoading = false;
              vm.validatePostNmvtisCall();
              clearInterval(timer);
            }

            if (counter >= vm.nmvtisRetryCount) {
              vm.setNmvtisTimeout(vm.receivedNmvtisData);
              vm.isLoading = false;
              clearInterval(timer);
            }
          })
          .catch(errors => {
            vm.isLoading = false;
            clearInterval(timer);
            vm.nmvtisResult = "Request not processed.";
            if (this.$_.isArray(errors)) {
              errors.forEach(error => {
                vm.addError(error.errorCode);
              });
            }
            vm.addError(2268);
          this.handleException(errors);
          });
      }, vm.nmvtisWaitInterval);
    },
    ncicPolling(key) {
      var counter = 0;
      let vm = this;
      var timer = setInterval(function() {
        counter++;
        httpUtil
          .get("/interfaces/pollingncicstatus/" + key)
          .then(response => {
            if (response.data) {
              vm.receivedNcicData = true;
              vm.isNcicLoading = false;
              vm.contextInfo.ncicMessage = " VIN : " + vm.vehicle.vinNum + " : " +response.data;
              vm.removeError(20074);
              vm.removeError(21139);
              if (response.data.toUpperCase().includes(CONSTANTS.NCIC_ERROR_MSG_STOLEN_IN_CANADA)) {
                // As per the discussion with FT
                vm.addError(21139);
              }
              else if (!response.data.toUpperCase().includes(CONSTANTS.NO_RECORD)) {
                //BRU37568 : according to function team(shilpa) any message other than No record is considered as stolen.
                vm.addError(20074);
              }
              clearInterval(timer);
              vm.isNcicLoading = false;
            }
            if (counter >= vm.ncicRetryCount) {
              vm.setNcicTimeout(vm.receivedNcicData);
              vm.isNcicLoading = false;
              clearInterval(timer);
            }
          })
          .catch(errors => {
            vm.isNcicLoading = false;
            clearInterval(timer);
            if (this.$_.isArray(errors)) {
              errors.forEach(error => {
                vm.addError(error.errorCode);
              });
            }
            this.handleException(errors);
            vm.addError(21119);
          });
      }, vm.ncicWaitInterval);
    },
    validateBrandMessage() {
      if (this.receivedNmvtisData == true) {
        if (!this.brandData || this.brandData.length == 0) {
          this.contextInfo.additionalBrandMessages.pushOnce(
            CONSTANTS.NO_BRANDS_TO_SHOW
          );
        } else if (
          !this.contextInfo.additionalBrandMessages ||
          this.contextInfo.additionalBrandMessages.length == 0
        ) {
          this.contextInfo.additionalBrandMessages.pushOnce(
            CONSTANTS.BRANDS_TO_SHOW
          );
        }
      }

      if (this.SOR === CONSTANTS.SOR_CARATS) {
        if (
          this.contextInfo.nmvtisVerificationFlag == true &&
          this.fullBrandList.length > 8
        ) {
          this.contextInfo.additionalBrandMessages.pushOnce(
            CONSTANTS.MORE_THAN_EIGHT_BRANDS
          );
        }
      }
    },
    setNcicTimeout(receivedNcicData) {
      if (receivedNcicData == false) {
        this.addError(21118);
      } else {
        this.removeError(21118);
      }
    },
    async setNmvtisNcicRetryCounts() {
      this.requestBody.refTypeTxt = "7005 - SYSTEM PROPERTIES";
      var url = "/referencedata/paginatedRefData/";
      let vm = this;
      var refCd, refCdDesc;
      await httpUtil
        .post(url, vm.requestBody)
        .then(response => {
          if (response.data && response.data.length > 0) {
            for (var i = 0; i < response.data.length; i++) {
              refCd = response.data[i].referenceCode;
              refCdDesc = response.data[i].referenceCodeDesc;
              if (refCd == CONSTANTS.REF_NCIC_RETRY_COUNT) {
                vm.ncicRetryCount = refCdDesc;
              } else if (refCd == CONSTANTS.REF_NMVTIS_RETRY_COUNT) {
                vm.nmvtisRetryCount = refCdDesc;
              } else if (refCd == CONSTANTS.REF_NCIC_WAIT_TIME_MS) {
                vm.ncicWaitInterval = refCdDesc;
              } else if (refCd == CONSTANTS.REF_NMVTIS_WAIT_TIME_MS) {
                vm.nmvtisWaitInterval = refCdDesc;
              }
            }
          }
        })
        .catch(error => {
          this.handleException(error);
        });
    }
  }
};
</script>
<style lang="scss" scoped>
/deep/ {
  .autocomplete-list {
    z-index: 2 !important;
  }
}
</style>






