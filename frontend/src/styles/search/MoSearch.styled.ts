import styled from 'styled-components'
import { breakpoints, palette, zindex } from '../../constants/Styles'

export const MoSearchContainer = styled.div`
  position: fixed;
  display: flex;
  flex-direction: column;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: ${palette.white};
  z-index: ${zindex.header + 1};
  overflow-y: scroll;
  overflow-x: hidden;

  ${breakpoints.large} {
    display: none;
  }
`

export const SearchHeader = styled.section`
  display: flex;
  width: 100%;
  height: 6rem;
  box-shadow: 0.1rem 0.5rem 1.1rem ${palette.shadow};
  margin-bottom: 5rem;
`

export const CloseButton = styled.button`
  padding: 0rem 2rem;
  border: none;
  cursor: pointer;
`

export const SearchHeaderTxt = styled.div`
  display: flex;
  position: relative;
  left: 50%;
  transform: translate(-25%);
  width: 100%;
  align-items: center;
  font-size: 2rem;

  & > p {
    font-weight: 600;
  }
`
