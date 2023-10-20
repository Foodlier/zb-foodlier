/* eslint-disable import/prefer-default-export */
// 모달창 UI
import styled from 'styled-components'

export const AuthModalWrapper = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
`
export const AuthModalOverlay = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  z-index: 9999;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);

  &.fade-enter {
    opacity: 0;
  }
  &.fade-enter-active {
    opacity: 1;
    transition: opacity 300ms;
  }
  &.fade-exit {
    opacity: 1;
  }
  &.fade-exit-active {
    opacity: 0;
    transition: opacity 300ms;
  }
`
export const AuthModal = styled.div`
  position: relative;
  z-index: 10000;
  width: 400px;
  height: 400px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 0 6px 0 rgba(0, 0, 0, 0.5);
  overflow: hidden;

  &.fade-enter {
    transform: translateY(-100%);
  }
  &.fade-enter-active {
    transform: translateY(0);
    transition: transform 300ms;
  }
  &.fade-exit {
    transform: translateY(0);
  }
  &.fade-exit-active {
    transform: translateY(-100%);
    transition: transform 300ms;
  }
`
export const AuthModalHeader = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px;
  border-bottom: 1px solid #e9ecef;
`
export const AuthModalTitle = styled.h3`
  font-size: 1.5rem;
  font-weight: bold;
`
export const AuthModalBody = styled.div`
  padding: 20px;
`
export const AuthModalFooter = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px;
  border-top: 1px solid #e9ecef;
`
export const AuthModalCloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  border: none;
  border-radius: 50%;
  background-color: #dee2e6;
  cursor: pointer;
  outline: none;
`
