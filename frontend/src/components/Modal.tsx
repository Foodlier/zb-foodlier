// import styled from 'styled-components'
// import { breakpoints } from '../constants/Styles'

// interface ModalProps {
//   setModalOpen: React.Dispatch<React.SetStateAction<boolean>>
// }

// const GreyBackground = styled.div`
//   position: absolute;
//   top: 0;
//   left: 0;
//   width: 100vw;
//   height: 100%;
//   background-color: #000000;
//   opacity: 0.4;
//   z-index: 99;
// `

// const ModalScreen = styled.div`
//   position: absolute;
//   top: 50%;
//   left: 50%;
//   transform: translate(-50%, -50%);
//   width: 90%;
//   height: 80%;
//   max-width: 1000px;
//   background-color: #ffffff;
//   padding: 5%;
//   border-radius: 10px;
//   z-index: 100;
//   ${breakpoints.large} {
//     padding: 3%;
//     width: 70%;
//   }
// `

// const ModalTop = styled.div`
//   display: flex;
//   justify-content: space-between;
//   span,
//   button {
//     font-size: 20px;
//     font-weight: bold;
//   }
// `

// const Modal: React.FC<ModalProps> = ({ setModalOpen }) => {
//   const closeModal = () => {
//     setModalOpen(false)
//   }

//   return (
//     <>
//       <GreyBackground />
//       <ModalScreen>
//         <ModalTop>
//           <span>요청서 목록</span>
//           <button type="button" onClick={closeModal}>
//             X
//           </button>
//         </ModalTop>
//         <div>
//           <ul>
//             <li></li>
//           </ul>
//         </div>
//       </ModalScreen>
//     </>
//   )
// }

// export default Modal
