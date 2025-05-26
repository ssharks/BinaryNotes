/*
 Copyright 2006-2011 Abdulla Abdurakhmanov (abdulla@latestbit.com)
 Original sources are available at www.latestbit.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

using its.cam;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using org.bn.coders.test_asn;
using System.Text;

namespace org.bn.coders.per
{
    [TestClass]
    public class PERUnalignedDecoderTest : DecoderTest
    {
        protected readonly CoderFactory coderFactory = new CoderFactory();

        public PERUnalignedDecoderTest()
            : base(new PERUnalignedCoderTestUtils())
        {
        }

        protected override IDecoder newDecoder()
        {
            return coderFactory.newDecoder("PER/Unaligned");
        }

        private void CheckCamResults(its.cam.CAM got, its.cam.CAM exp)
        {
            Assert.IsNotNull(got);

            Assert.AreEqual(got.Header.ProtocolVersion, exp.Header.ProtocolVersion);
            Assert.AreEqual(got.Header.MessageID, exp.Header.MessageID);
            Assert.AreEqual(got.Header.StationID.Value, exp.Header.StationID.Value);
            Assert.AreEqual(got.Cam.GenerationDeltaTime.Value, exp.Cam.GenerationDeltaTime.Value);

            var gotBasic = got.Cam.CamParameters.BasicContainer;
            var expBasic = exp.Cam.CamParameters.BasicContainer;
            Assert.AreEqual(gotBasic.StationType.Value, expBasic.StationType.Value);
            Assert.AreEqual(gotBasic.ReferencePosition.Latitude.Value, expBasic.ReferencePosition.Latitude.Value);
            Assert.AreEqual(gotBasic.ReferencePosition.Longitude.Value, expBasic.ReferencePosition.Longitude.Value);
            
            var gotPosEclipse = gotBasic.ReferencePosition.PositionConfidenceEllipse;
            var expPosEclipse = expBasic.ReferencePosition.PositionConfidenceEllipse;
            Assert.AreEqual(gotPosEclipse.SemiMajorConfidence.Value, expBasic.ReferencePosition.PositionConfidenceEllipse.SemiMajorConfidence.Value);
            Assert.AreEqual(gotPosEclipse.SemiMinorConfidence.Value, expPosEclipse.SemiMinorConfidence.Value);
            Assert.AreEqual(gotPosEclipse.SemiMajorOrientation.Value, expPosEclipse.SemiMajorOrientation.Value);
            Assert.AreEqual(gotBasic.ReferencePosition.Altitude.AltitudeValue.Value, expBasic.ReferencePosition.Altitude.AltitudeValue.Value);
            Assert.AreEqual(gotBasic.ReferencePosition.Altitude.AltitudeConfidence.Value, expBasic.ReferencePosition.Altitude.AltitudeConfidence.Value);
            
            var gotHighFreq = got.Cam.CamParameters.HighFrequencyContainer.BasicVehicleContainerHighFrequency;
            var expHighFreq = exp.Cam.CamParameters.HighFrequencyContainer.BasicVehicleContainerHighFrequency;
            Assert.AreEqual(gotHighFreq.Heading.HeadingValue.Value,
                        expHighFreq.Heading.HeadingValue.Value);
            Assert.AreEqual(gotHighFreq.Heading.HeadingConfidence.Value,
                        expHighFreq.Heading.HeadingConfidence.Value);

            Assert.AreEqual(gotHighFreq.Speed.SpeedValue.Value,
                        expHighFreq.Speed.SpeedValue.Value);
            Assert.AreEqual(gotHighFreq.Speed.SpeedConfidence.Value,
                        expHighFreq.Speed.SpeedConfidence.Value);

            Assert.AreEqual(gotHighFreq.DriveDirection.Value,
                        expHighFreq.DriveDirection.Value);

            Assert.AreEqual(gotHighFreq.VehicleLength.VehicleLengthValue.Value,
                        expHighFreq.VehicleLength.VehicleLengthValue.Value);
            Assert.AreEqual(gotHighFreq.VehicleLength.VehicleLengthConfidenceIndication.Value,
                        expHighFreq.VehicleLength.VehicleLengthConfidenceIndication.Value);

            Assert.AreEqual(gotHighFreq.VehicleWidth.Value,
                        expHighFreq.VehicleWidth.Value);

            Assert.AreEqual(gotHighFreq.LongitudinalAcceleration.LongitudinalAccelerationValue.Value,
                        expHighFreq.LongitudinalAcceleration.LongitudinalAccelerationValue.Value);
            Assert.AreEqual(gotHighFreq.LongitudinalAcceleration.LongitudinalAccelerationConfidence.Value,
                        expHighFreq.LongitudinalAcceleration.LongitudinalAccelerationConfidence.Value);

            Assert.AreEqual(gotHighFreq.Curvature.CurvatureValue.Value,
                        expHighFreq.Curvature.CurvatureValue.Value);
            Assert.AreEqual(gotHighFreq.Curvature.CurvatureConfidence.Value,
                        expHighFreq.Curvature.CurvatureConfidence.Value);
            Assert.AreEqual(gotHighFreq.CurvatureCalculationMode.Value,
                        expHighFreq.CurvatureCalculationMode.Value);


            Assert.AreEqual(gotHighFreq.YawRate.YawRateValue.Value,
                        expHighFreq.YawRate.YawRateValue.Value);
            Assert.AreEqual(gotHighFreq.YawRate.YawRateConfidence.Value,
                        expHighFreq.YawRate.YawRateConfidence.Value);
        }

        [TestMethod]
        public void testCamDecoding()
        {
            var decoder = newDecoder();
            System.IO.MemoryStream stream = new System.IO.MemoryStream(PERUnalignedCoderTestUtils.createCamBytes());

            var got = decoder.decode<its.cam.CAM>(stream);
            var exp = PERUnalignedCoderTestUtils.createCam();

            CheckCamResults(got, exp);
        }

        public void testCamEncodingDecoding()
        {
            var decoder = newDecoder();
            System.IO.MemoryStream stream = new System.IO.MemoryStream(PERUnalignedCoderTestUtils.createCamBytes());

            IEncoder encoder = new PERUnalignedEncoder();

            System.IO.MemoryStream outputStream = new System.IO.MemoryStream();
            var exp = PERUnalignedCoderTestUtils.createCam();
            encoder.encode(exp, outputStream);

            outputStream.Seek(0, SeekOrigin.Begin);
            var got = decoder.decode<its.cam.CAM>(outputStream);

            CheckCamResults(got, exp);
        }
    }
}