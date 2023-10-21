import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const Container = styled.div`
  padding: 2rem 2rem 10rem;
`

export const Info = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 2rem;
`
export const SubTitle = styled.span`
  color: ${palette.textPrimary};
  font-weight: ${weight.subTitle};
  font-size: ${typography.mobile.subTitle};

  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`

export const SelectBox = styled.div`
  width: 15rem;
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  padding: 0.5rem 1rem;
  text-align: left;
`

export const OptionList = styled.ul<{ $toggle: boolean }>`
  display: ${props => (props.$toggle ? 'block' : 'none')};
  width: 100%;
  position: absolute;
  top: 30px;
  left: 0;
  background-color: ${palette.white};
  border: 1px solid ${palette.divider};
  padding-left: 0;
  border-radius: 5px;
`

export const Option = styled.li`
  &:hover {
    background-color: ${palette.divider};
  }
`

export const OptionButton = styled.button`
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  text-align: right;
  padding: 0 4px;
`

export const CardList = styled.ul`
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 500px;
  overflow: auto;
  padding-left: 0;
  &::-webkit-scrollbar {
    display: none;
  }
`

export const WritingButton = styled.button`
  color: ${palette.main};
  padding: 0 2rem;
`

export const ButtonList = styled.div`
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: ${palette.white};
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid ${palette.divider};
  padding: 2rem;

  button {
    margin-bottom: 110px;
    ${breakpoints.large} {
      margin-bottom: 10px;
    }
  }
`
